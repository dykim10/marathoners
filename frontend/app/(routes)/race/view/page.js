"use client";

import { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";
import { useSession } from "next-auth/react";
import { Button, Container, Card } from "react-bootstrap";

export default function RaceDetailPage() {
    const { mrUuid } = useParams();
    const router = useRouter();
    const { data: session } = useSession(); // 사용자 세션 확인
    const [race, setRace] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchRaceDetails = async () => {
            try {
                const res = await fetch(`/api/race/view/${mrUuid}`);
                if (!res.ok) throw new Error("Failed to fetch race details");

                const data = await res.json();
                setRace(data);
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchRaceDetails();
    }, [mrUuid]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error loading race details.</p>;

    return (
        <Container className="mt-4">
            <Card>
                <Card.Body>
                    <Card.Title>{race.mrName}</Card.Title>
                    <Card.Text><strong>시작일:</strong> {new Date(race.mrStartDt).toLocaleDateString()}</Card.Text>
                    <Card.Text><strong>장소:</strong> {race.mrLocation}</Card.Text>
                    <Card.Text><strong>주최:</strong> {race.mrCompany}</Card.Text>
                    <Card.Text><strong>설명:</strong> {race.mrContent}</Card.Text>
                    {race.mrHomepageUrl && (
                        <Card.Text>
                            <strong>홈페이지:</strong>
                            <a href={race.mrHomepageUrl} target="_blank" rel="noopener noreferrer">
                                {race.mrHomepageUrl}
                            </a>
                        </Card.Text>
                    )}
                </Card.Body>
            </Card>

            {/* 버튼 그룹 */}
            <div className="d-flex justify-content-between mt-3">
                {/* 목록 버튼 */}
                <Button variant="secondary" onClick={() => router.push("/race/list")}>
                    목록
                </Button>

                {/* 수정 버튼 (관리자만 표시) */}
                {session?.user?.role === "admin" && (
                    <Button variant="warning" onClick={() => router.push(`/race/edit/${mrUuid}`)}>
                        수정
                    </Button>
                )}
            </div>
        </Container>
    );
}
