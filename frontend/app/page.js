"use client";  // ⬅️ 클라이언트 컴포넌트로 선언

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function HomePage() {
    const router = useRouter();
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem("token");
        setIsLoggedIn(!!token);
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("token");
        setIsLoggedIn(false);
        router.push("/login");
    };

    return (
        <div className="container">
            <h1>Marathoners 메인페이지</h1>
            <p>주요기술 : Next.Js, Spring-Boot, PostgreSql</p>
            {isLoggedIn ? (
                <>
                    <p>로그인 상태입니다.</p>
                    <button onClick={handleLogout}>로그아웃</button>
                </>
            ) : (
                <button onClick={() => router.push("/login")}>로그인</button>
            )}

            <style jsx>{`
                .container {
                    text-align: center;
                    margin-top: 50px;
                }
                button {
                    padding: 10px 20px;
                    background-color: #007bff;
                    color: white;
                    border: none;
                    cursor: pointer;
                    margin-top: 10px;
                }
            `}</style>
        </div>
    );
}
