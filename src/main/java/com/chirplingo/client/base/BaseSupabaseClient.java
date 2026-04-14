package com.chirplingo.client.base;

import io.github.jan.supabase.SupabaseClient;

public abstract class BaseSupabaseClient {
    protected SupabaseClient sdk;

    public BaseSupabaseClient(SupabaseClient sdk) {
        this.sdk = sdk;
    }
}
