"use client";

import 'bootstrap/dist/css/bootstrap.min.css';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from "react";
import { Container, Row, Col, Button, Card } from 'react-bootstrap';

export default function Home() {
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
        <Container className="mt-4">
            <Row className="align-items-center">
                <Col md={6}>
                    <div>
                        <h1 className="fw-bold">Marathoners 메인페이지</h1>
                        <p className="text-muted">주요기술 : Next.Js, Spring-Boot, PostgreSql</p>
                        {isLoggedIn ? (
                            <>
                                <p className="text-success">로그인 상태입니다.</p>
                                <Button variant="danger" onClick={handleLogout}>로그아웃</Button>
                            </>
                        ) : (
                            <Button variant="primary" onClick={() => router.push('/login')}>로그인</Button>
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
