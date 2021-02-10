// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.azuresamples.msal4j.helpers;

import com.microsoft.aad.msal4j.IAccount;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.graph.models.extensions.Group;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.SignedJWT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class defines all auth-related session properties that are required MSAL
 * Java apps using this sample repo's paradigm will require this.
 */
public class IdentityContextData implements Serializable {
    private static final long serialVersionUID = 2L;
    private String nonce = null;
    private String state = null;
    private Date stateDate = null;
    private String policy = null;
    private boolean authenticated = false;
    private String username = null;
    private String accessToken = null;
    private String idToken = null;
    private List<String> idTokenGroups = new ArrayList<>();
    private IAccount account = null;
    private Map<String, Object> idTokenClaims = new HashMap<>();
    private String tokenCache = null;
    private IAuthenticationResult authResult = null;
    private boolean hasChanged = false;

    public void clear() {
        nonce = null;
        state = null;
        stateDate = null;
        policy = null;
        authenticated = false;
        username = null;
        accessToken = null;
        idToken = null;
        idTokenGroups = new ArrayList<>();
        account = null;
        idTokenClaims = new HashMap<>();
        tokenCache = null;
        authResult = null;
        setHasChanged(true);
    }

    public boolean hasChanged() {
        return hasChanged;
    }
    public void setHasChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }

    public IAccount getAccount() {
        return account;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Map<String, Object> getIdTokenClaims() {
        return idTokenClaims;
    }

    public List<String> getIdTokenGroups() {
        return this.idTokenGroups;
    }

    public String getTokenCache() {
        return this.tokenCache;
    }

    public String getUsername() {
        return username;
    }

    public boolean getAuthenticated() {
        return authenticated;
    }

    public String getNonce() {
        return this.nonce;
    }

    public String getState() {
        return this.state;
    }

    public String getPolicy() {
        return this.policy;
    }

    public Date getStateDate() {
        return this.stateDate;
    }

    public IAuthenticationResult getAuthResult() {
        return this.authResult;
    }

    public void setIdTokenClaims(String rawIdToken) throws java.text.ParseException {
        final Map<String, Object> tokenClaims = SignedJWT.parse(rawIdToken).getJWTClaimsSet().getClaims();
        // this.idTokenClaims = new HashMap<>();
        // tokenClaims.forEach((String claim, Object value) -> {
            // String val = value.toString();
            // this.idTokenClaims.put(claim, val);
        // });
        this.idTokenClaims = tokenClaims;

        JSONArray groups = (JSONArray)this.idTokenClaims.get("groups");
        if (groups != null) {
            groups.forEach(elem -> this.idTokenGroups.add((String)elem));
        }
        this.setHasChanged(true);
    }

    public void setIdTokenGroups(Map<String,Object> idTokenClaims) {
        this.idTokenGroups = new ArrayList<>();

        JSONArray groups = (JSONArray)this.idTokenClaims.get("groups");
        if (groups != null) {
            groups.forEach(elem -> this.idTokenGroups.add((String)elem));
        } else {
            // check for potential groups overage scenario!
            JSONObject jsonObj = (JSONObject)idTokenClaims.get("_claim_names");
            if (jsonObj != null && jsonObj.containsKey("groups")) {
                // overage scenario exists, handle it:
                this.idTokenClaims.put("_groups_overage", "error");
            }
        }
    }

    public void setGroups(List<Group> groups) {
        this.idTokenGroups = new ArrayList<>();

        Iterator<Group> it = groups.iterator();
        while(it.hasNext()){
            this.idTokenGroups.add(it.next().id);
        }
    }

    public void clearIdTokenClaims() {
        this.idTokenClaims = new HashMap<>();
        this.setHasChanged(true);
    }

    public void setTokenCache(final String serializedTokenCache) {
        this.tokenCache = serializedTokenCache;
        this.setHasChanged(true);
    }

    public void setUsername(final String username) {
        this.username = username;
        this.setHasChanged(true);
    }

    public void setAuthenticated(final boolean authenticated) {
        this.authenticated = authenticated;
        this.setHasChanged(true);
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
        this.setHasChanged(true);
    }

    public void setState(final String state) {
        this.state = state;
        this.stateDate = new Date();
        this.setHasChanged(true);
    }

    // policy is relevant for AAD B2C apps only.
    public void setPolicy(final String policy) {
        this.policy = policy;
        this.setHasChanged(true);
    }

    public void setStateAndNonceAndPolicy(String state, String nonce, String policy) {
        this.state = state;
        this.nonce = nonce;
        this.stateDate = new Date();
        this.policy = policy;
        this.setHasChanged(true);
    }

    public void setStateAndNonce(String state, String nonce) {
        this.state = state;
        this.nonce = nonce;
        this.stateDate = new Date();
        this.setHasChanged(true);
    }

    public void setAuthResult(IAuthenticationResult authResult, String serializedTokenCache)
            throws java.text.ParseException {
        this.authResult = authResult;
        this.setAccount(authResult.account());
        this.idToken = authResult.idToken();
        this.setAccessToken(authResult.accessToken());
        this.tokenCache = serializedTokenCache;
        setIdTokenClaims(this.idToken);
        this.username = (String)this.idTokenClaims.get("name");
        this.authenticated = true;

        this.setHasChanged(true);
    }

    public void setAccount(IAccount account) {
        this.account = account;
        this.setHasChanged(true);
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        this.setHasChanged(true);
    }
}
