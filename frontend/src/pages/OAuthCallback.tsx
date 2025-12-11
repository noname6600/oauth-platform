import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { exchangeOAuthCode } from "../api/auth";

export default function OAuthCallback() {
  const navigate = useNavigate();

useEffect(() => {
  const doExchange = async () => {
    const params = new URLSearchParams(window.location.search);
    const code = params.get("code");
    if (!code) return;

    await exchangeOAuthCode(code);
    navigate("/profile");
  };

  doExchange();
}, [navigate]);

  return <div>Logging in with Google...</div>;
}
