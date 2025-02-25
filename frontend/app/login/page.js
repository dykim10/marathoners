"use client";
/**
 *  useState를 사용하면 input 값이 변경될 때 자동으로 React 상태(state)에 저장됩니다.
 */
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Container, Form, Button, Card, Alert } from "react-bootstrap"; //

export default function LoginPage() {
    const router = useRouter();
    const [userId, setUserId] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    // ✅ 세션 확인 함수 추가
    const checkSession = async () => {
        try {
            const response = await fetch("/api/session", {
                method: "GET",
                credentials: "include", // 🔹 쿠키 포함 요청
            });

            console.log("🔹 `/api/session` 요청 헤더:", response.headers);
            console.log("🔹 `/api/session` 응답 상태:", response.status);

            if (!response.ok) {
                throw new Error("세션 없음::login/page.js");
            }

            const data = await response.json();
            console.log("✅ 세션 유지됨, 서버 응답:", data);
            setIsLoggedIn(true); // ✅ 로그인 상태 업데이트
        } catch (error) {
            console.log("❌ 세션 확인 실패 123 => ", error);
            setIsLoggedIn(false);
        }
    };

    // ✅ 페이지 로드 시 세션 확인
    useEffect(() => {
        checkSession();
    }, []);

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
            console.log("🔹 로그인 요청 후 JSESSIONID 확인:", document.cookie);
            console.log("data ==> ", data);

            checkSession(); // ✅ 로그인 후 세션 체크 실행

            router.push("/");
        } catch (error) {
            setError(error.message);
        }
    };

    return (
        <Container className="d-flex justify-content-center align-items-center vh-100">
            <Card className="p-4 shadow-sm" style={{ width: "350px" }}>
                <Card.Body>
                    <h2 className="text-center mb-4">로그인</h2>
                    {error && <Alert variant="danger">{error}</Alert>}
                    <Form onSubmit={handleLogin}>
                        <Form.Group className="mb-3" controlId="userId">
                            <Form.Label>아이디</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="아이디 입력"
                                value={userId}
                                onChange={(e) => setUserId(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="password">
                            <Form.Label>비밀번호</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="비밀번호 입력"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Button variant="primary" type="submit" className="w-100">
                            로그인
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>

    );
}
