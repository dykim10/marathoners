"use client";

import { Container, Row, Col, Card } from 'react-bootstrap';

export default function IntroducePage() {
    return (
        <Container className="py-5">
            <h1 className="text-center mb-4">사이트 소개</h1>

            <Row className="mb-4">
                <Col md={{ span: 8, offset: 2 }}>
                    <Card className="shadow-sm p-3">
                        <Card.Body>
                            <Card.Title className="fw-bold">사이트 개발 기술</Card.Title>
                            <Card.Text>
                                이 사이트는 최신 웹 기술을 활용하여 개발되었습니다.
                            </Card.Text>
                                <div>
                                    <ul>
                                        <li>Frontend: Next.js 15.1.7 (App Router 방식) / react 19.0.0</li>
                                        <li>Backend: Spring Boot 3.4</li>
                                        <li>Database: PostgreSQL</li>
                                        <li>ORM: MyBatis + Lombok</li>
                                        <li>UI Framework: React-Bootstrap</li>
                                        <li>Programming Language: Java 17, JavaScript</li>
                                    </ul>
                                </div>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Row>
                <Col md={{ span: 8, offset: 2 }}>
                    <Card className="shadow-sm p-3">
                        <Card.Body>
                            <Card.Title className="fw-bold">사이트 개발 목표</Card.Title>
                            <Card.Text>
                                이 사이트는 마라톤 대회의 관련 정보를 제공하고,
                                참가자들이 쉽게 정보를 얻을 수 있도록 하고자 합니다.
                                특히, 참가자들이 남긴 리뷰를 빅데이터화 한다.
                            </Card.Text>
                                <div>
                                    <ul>
                                        <li>사용자 친화적인 UI/UX</li>
                                        <li>빠르고 안정적인 서비스 제공</li>
                                        <li>효율적인 데이터 관리 및 검색 기능</li>
                                        <li>마라톤 일정 및 기록 관리 시스템 구축</li>
                                        <li>참가자 커뮤니티 기능 추가</li>
                                    </ul>
                                </div>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}
