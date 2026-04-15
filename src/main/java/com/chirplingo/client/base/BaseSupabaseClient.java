package com.chirplingo.client.base;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;

public abstract class BaseSupabaseClient {
    protected SupabaseContext context;

    public BaseSupabaseClient(SupabaseContext context) {
        this.context = context;
    }

    protected HttpRequest.Builder createRequest(String endpoint) {
        String url = context.getUrl() + endpoint;
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("apikey", context.getKey())
                .header("Authorization", "Bearer " + context.getKey())
                .header("Content-Type", "application/json");
    }
}

/*
CREATE TABLE public.learn_history (
  user_id uuid NOT NULL,
  monday integer DEFAULT 0,
  tuesday integer DEFAULT 0,
  wednesday integer DEFAULT 0,
  thursday integer DEFAULT 0,
  friday integer DEFAULT 0,
  saturday integer DEFAULT 0,
  sunday integer DEFAULT 0,
  updated_at timestamp with time zone DEFAULT now(),
  streak integer DEFAULT 0,
  CONSTRAINT learn_history_pkey PRIMARY KEY (user_id),
  CONSTRAINT learn_history_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.profiles(id)
);
CREATE TABLE public.profiles (
  id uuid NOT NULL,
  email text,
  user_name text,
  avatar text,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  CONSTRAINT profiles_pkey PRIMARY KEY (id),
  CONSTRAINT profiles_id_fkey FOREIGN KEY (id) REFERENCES auth.users(id)
);
CREATE TABLE public.todo_list (
  id uuid NOT NULL DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL,
  content text NOT NULL,
  is_finished boolean DEFAULT false,
  deadline timestamp with time zone,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  deleted_at timestamp with time zone,
  CONSTRAINT todo_list_pkey PRIMARY KEY (id),
  CONSTRAINT todo_list_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.profiles(id)
);
CREATE TABLE public.vocabularies (
  id uuid NOT NULL DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL,
  word text NOT NULL,
  meaning text NOT NULL,
  ipa text,
  type text,
  example text,
  note text,
  repetitions integer DEFAULT 0,
  interval_days integer DEFAULT 0,
  ease_factor double precision DEFAULT 2.5,
  next_review_at timestamp with time zone DEFAULT now(),
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  deleted_at timestamp with time zone,
  CONSTRAINT vocabularies_pkey PRIMARY KEY (id),
  CONSTRAINT vocabularies_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.profiles(id)
);
*/