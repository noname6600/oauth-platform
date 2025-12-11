export type AuthMode = "jwt" | "basic";

const LS_KEY = "auth_mode";

let currentAuthMode: AuthMode =
  (localStorage.getItem(LS_KEY) as AuthMode) ||
  (import.meta.env.VITE_DEFAULT_AUTH_MODE as AuthMode) ||
  "jwt";

export let BASE_URL = currentAuthMode === "jwt"
  ? import.meta.env.VITE_API_BASE_JWT
  : import.meta.env.VITE_API_BASE_BASIC;

export function setAuthMode(mode: AuthMode) {
  currentAuthMode = mode;
  localStorage.setItem("auth_mode", mode);
  BASE_URL = mode === "jwt"
    ? import.meta.env.VITE_API_BASE_JWT
    : import.meta.env.VITE_API_BASE_BASIC;
}

export function getAuthMode(): AuthMode | null {
  const stored = localStorage.getItem("auth_mode") as AuthMode | null;
  return stored;
}

export function getGoogleOAuthUrl(): string | null {
  if (getAuthMode() !== "jwt") return null;

  const backend = import.meta.env.VITE_API_BASE_JWT;
  const redirect = `${window.location.origin}/oauth/callback`;

  return `${backend}/oauth2/authorization/google?redirect_uri=${encodeURIComponent(
    redirect
  )}`;
}