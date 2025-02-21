"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

export default function LoginPage() {
    const router = useRouter();
    const [userId, setUserId] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const handleLogin = async (e) => {
        e.preventDefault();
        setError("");

        try {
            // Next.js API Route 호출 (Spring Boot API가 아니라!)
            const response = await fetch("/api/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json",
                },
                credentials: "include",
                body: JSON.stringify({ userId, password }),
            });

            if (!response.ok) {
                throw new Error("로그인 실패. 아이디와 비밀번호를 확인하세요.");
            }

            const data = await response.json();
            localStorage.setItem("token", data.token);
            router.push("/");
        } catch (error) {
            setError(error.message);
        }
    };

    return (
        <div className="login-container">
            <h1>로그인</h1>
            {error && <p className="error">{error}</p>}
            <form onSubmit={handleLogin}>
                <input
                    type="text"
                    id="userId"
                    placeholder="아이디"
                    value={userId}
                    onChange={(e) => setUserId(e.target.value)}
                    required
                />
                <input
                    type="password"
                    id="userPassword"
                    placeholder="비밀번호"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit">로그인</button>
            </form>

            <style jsx>{`
                .login-container {
                    text-align: center;
                    margin-top: 50px;
                }
                input {
                    display: block;
                    margin: 10px auto;
                    padding: 10px;
                    width: 200px;
                }
                .error {
                    color: red;
                }
            `}</style>
        </div>
    );
}
