"use client";

import 'bootstrap/dist/css/bootstrap.min.css';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from "react";
import { Container, Row, Col, Button, Card } from 'react-bootstrap';
import { checkSession } from "@/utils/session";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

export default function Home() {
    const router = useRouter();
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [raceList, setRaceList] = useState([]); // ✅ 대회 데이터 저장

    useEffect(() => {
        const verifySession = async () => {
            try {
                const sessionExists = await checkSession();
                setIsLoggedIn(sessionExists);
            } catch (error) {
                console.error("세션 확인 중 오류 발생:", error);
            }
        };

        verifySession();
        fetchRaceList();
    }, []);

    const fetchRaceList = async () => {
        try {
            const res = await fetch("/api/race/list", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ page: 1, rows: 5 }) // 최신 5개만 불러오기
            });

            if (!res.ok) throw new Error("Failed to fetch race list");

            const data = await res.json();
            setRaceList(data.raceList || []); // 데이터 저장
        } catch (error) {
            console.error("Error fetching races:", error);
        }
    };

    const handleNavigation = (path) => {
        router.push(path);
    };

    const sliderSettings = {
        dots: true,
        infinite: true,
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: 3000,
        arrows: false,
    };

    return (
        <Container className="mt-5 text-center">
            {/* ✅ 헤딩 섹션 */}
            <h1 className="fw-bold">Marathoners</h1>
            <hr />

            {/* ✅ 대회 일정 스와이프 섹션 */}
            <Container className="mb-5">
                {raceList.length > 0 ? (
                    <Slider {...sliderSettings}>
                        {raceList.map((race, index) => (
                            <Card key={index} className="shadow-sm">
                                <Card.Body>
                                    <Card.Title>{race.mrName}</Card.Title>
                                    <Card.Text>
                                        <strong>일정:</strong> {new Date(race.mrStartDt).toLocaleDateString()}
                                    </Card.Text>
                                    <Card.Text>
                                        <strong>장소:</strong> {race.mrLocation}
                                    </Card.Text>
                                    <Button variant="primary" size="sm" onClick={() => router.push(`/race/view/${race.mrUuid}`)}>
                                        상세보기
                                    </Button>
                                </Card.Body>
                            </Card>
                        ))}
                    </Slider>
                ) : (
                    <p>대회 정보를 불러오는 중...</p>
                )}
            </Container>

            {/* ✅ 기능 버튼 섹션 */}
            <Container>
                <Row className="justify-content-center">
                    <Col md={3} className="mb-3">
                        <Button variant="outline-primary" size="lg" className="w-100" onClick={() => handleNavigation("/race/list")}>
                            대회 정보
                        </Button>
                    </Col>
                    <Col md={3} className="mb-3">
                        <Button variant="outline-success" size="lg" className="w-100" onClick={() => handleNavigation("/user/list")}>
                            회원 정보
                        </Button>
                    </Col>
                    <Col md={3} className="mb-3">
                        <Button variant="outline-warning" size="lg" className="w-100" onClick={() => handleNavigation("/feature3")}>
                            기타 기능
                        </Button>
                    </Col>
                </Row>
            </Container>
        </Container>
    );
}
