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
            const sessionExists = await checkSession(); //공통 함수 호출
            console.log("? => " + sessionExists);
            if (sessionExists) {
                setIsLoggedIn(true);
                console.log("세션 확인 완료, 비즈니스 로직 실행 가능");
                //TO-DO 비지니스 로직 실행 진행. 여기서 추가 개발 과정에서 참고 해야 할 코드


            } else {
                setIsLoggedIn(false);
                console.log("세션 없음, 로그인 필요");
            }
        };

        verifySession();
    }, []);

    //console.log(">> " + isLoggedIn);
    const handleLogout = async () => {
        try {
            //console.log("로그아웃 요청 시작: /api/logout");

            const response = await fetch("/api/logout", {
                method: "POST",
                credentials: "include",
            });

            //console.log("로그아웃 요청 응답 상태:", response.status);

            if (!response.ok) {
                throw new Error("로그아웃 실패");
            }

            // ✅ 로그아웃 후 `checkSession()` 실행하여 상태 업데이트
            const sessionExists = await checkSession();
            setIsLoggedIn(sessionExists);

            //console.log("로그아웃 후 세션 상태 확인:", sessionExists);

            if (!sessionExists) {
                router.push("/auth/login"); // 로그인 페이지로 이동
            }

        } catch (error) {
            console.error("로그아웃 실패:", error);
        }
    };

    const handleMyInfo = () => {
        router.push("/user/me"); //클릭 시 `/user/me` 페이지로 이동
    };

    return (
        <Container className="mt-4">
            <Row className="align-items-center">
                <Col md={6}>
                    <div>
                        <h1 className="fw-bold">Marathoners 메인페이지</h1>
                        <p className="text-muted">주요기술 : Next.Js, Spring-Boot, PostgreSql</p>
                        {isLoggedIn ? (
                            <>
                                <div>
                                    <p className="text-success">로그인 상태입니다.</p>
                                    <Button className="btn btn-danger btn-sm me-2" variant="danger" onClick={handleLogout}>로그아웃</Button>
                                    <Button className="btn btn-warning btn-sm" variant="warning" onClick={handleMyInfo}>내 정보 보기</Button>
                                </div>
                            </>
                        ) : (
                            <Button variant="primary" onClick={() => router.push('/auth/login')}>로그인</Button>
                        )}
                    </div>
                </Col>
                <Col md={6} className="text-center">
                    {/*<img src="/hero-image.jpg" alt="Hero" className="img-fluid rounded shadow" />*/}
                </Col>
            </Row>

            <Container className="mt-5">
                <Row className="g-4">
                    {[
                        { title: "Feature 1", text: "Describe the feature in detail." },
                        { title: "Feature 2", text: "Highlight another great feature." },
                        { title: "Feature 3", text: "Explain the third feature concisely." }
                    ].map((feature, index) => (
                        <Col md={4} key={index}>
                            <Card className="h-100 shadow-sm">
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
