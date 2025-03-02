"use client";

import 'bootstrap/dist/css/bootstrap.min.css';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from "react";
import { Container, Row, Col, Button, Card } from 'react-bootstrap';
import {checkSession} from "@/utils/session";

export default function Home() {
    const router = useRouter();
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        //백엔드 세션 확인 (Spring Boot API 호출)
        const verifySession = async () => {
            try {
                const sessionExists = await checkSession();
                setIsLoggedIn(sessionExists);
            } catch (error) {
                console.error("세션 확인 중 오류 발생:", error);
            }
        };

        verifySession();
    }, []);

    const handleNavigation = (path) => {
        router.push(path);
    };

    //console.log(">> " + isLoggedIn);
    const handleLogout = async () => {
        try {
            const response = await fetch("/api/logout", {
                method: "POST",
                credentials: "include",
            });

            if (!response.ok) throw new Error("로그아웃 실패");

            // 로그아웃 후 `checkSession()` 실행하여 상태 업데이트
            const sessionExists = await checkSession();
            setIsLoggedIn(sessionExists);

            //console.log("로그아웃 후 세션 상태 확인:", sessionExists);

            if (!sessionExists) router.push("/auth/login"); // 로그인 페이지로 이동

        } catch (error) {
            console.error("로그아웃 실패:", error);
        }
    };

    const features = [
        { title: "대회 정보", text: "대회 정보 게시판 연결", path: "/race/list" },
        { title: "회원 정보", text: "회원 정보 게시판 연결", path: "/user/list" },
        { title: "Feature 3", text: "Explain the third feature concisely.", path: "/feature3" }
    ];

    return (
        <Container className="mt-4">
            <Row className="align-items-center">
                <Col md={6}>
                    <div>
                        <h1 className="fw-bold">Marathoners</h1>
                        <p className="text-muted">러너들을 위한 대회 참고서</p>
                    </div>
                </Col>
                <Col md={6} className="text-center">
                    {/*<img src="/hero-image.jpg" alt="Hero" className="img-fluid rounded shadow" />*/}
                </Col>
            </Row>

            {/* 기능 카드 목록 */}
            <Container className="mt-5">
                <Row className="g-4">
                    {features.map((feature, index) => (
                        <Col md={4} key={index}>
                            <Card
                                className="h-100 shadow-sm"
                                onClick={() => handleNavigation(feature.path)}
                                style={{ cursor: "pointer" }}
                            >
                                <Card.Body>
                                    <Card.Title>{feature.title}</Card.Title>
                                    <Card.Text>{feature.text}</Card.Text>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            </Container>
        </Container>
    );
}
