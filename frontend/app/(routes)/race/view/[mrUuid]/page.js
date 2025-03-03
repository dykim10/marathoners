"use client";

import { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";
import { Button, Container, Card, Table } from "react-bootstrap";
import { RACE_TYPES } from "@/constants/raceTypes"; // ✅ RACE_TYPES import

export default function RaceDetailPage() {
    const { mrUuid } = useParams();
    const router = useRouter();

    const [race, setRace] = useState(null);
    const [raceCourseList, setRaceCourseList] = useState([]);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchRaceDetails = async () => {
            try {
                const res = await fetch(`/api/race/view/${mrUuid}`);
                if (!res.ok) throw new Error("Failed to fetch race details");

                const data = await res.json();
                console.log(data);
                setRace(data.raceInfo);
                setRaceCourseList(data.raceCourseDetailList || []); //빈배열 삽입
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchRaceDetails();
    }, [mrUuid]);

    // ✅ mrCourseType을 label로 변환하는 함수
    const getRaceLabel = (courseType) => {
        const race = RACE_TYPES.find(r => r.key === courseType);
        return race ? race.label : "알 수 없음";
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error loading race details.</p>;

    return (
        <Container className="mt-4">
            {/* ✅ 기본 대회 정보 */}
            <Card className="mb-4">
                <Card.Body>
                    <Card.Title className="mb-3">{race.mrName}</Card.Title>
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

            {/* 추가 데이터 (코스 정보) */}
            {raceCourseList.length > 0 && (
                <Card className="mb-4">
                    <Card.Body>
                        <Card.Title className="mb-3">대회 코스 정보</Card.Title>
                        <Table striped bordered hover>
                            <thead>
                            <tr>
                                <th>코스 유형</th>
                                <th>참가 정원</th>
                                <th>참가비</th>
                                <th>버전</th>
                            </tr>
                            </thead>
                            <tbody>
                            {raceCourseList.map((course, index) => (
                                <tr key={index}>
                                    <td>{getRaceLabel(course.mrCourseType)}</td> {/* Label 변환 */}
                                    <td>{course.mrCourseCapacity}</td>
                                    <td>{course.mrCoursePrice} 원</td>
                                    <td>{course.mrCourseVersion}</td>
                                </tr>
                            ))}
                            </tbody>
                        </Table>
                    </Card.Body>
                </Card>
            )}

            {/* ✅ 리뷰 데이터 (추후 추가 예정) */}
            <Card className="mb-4">
                <Card.Body>
                    <Card.Title className="mb-3">대회 리뷰 (추후 추가 예정)</Card.Title>
                    <p>참가자들의 리뷰가 여기에 표시됩니다.</p>
                </Card.Body>
            </Card>

            {/* ✅ 버튼 그룹 */}
            <div className="d-flex justify-content-between mt-3">
                <Button variant="secondary" onClick={() => router.push("/race/list")}>
                    목록
                </Button>
                <Button variant="warning" onClick={() => router.push(`/race/edit/${mrUuid}`)}>
                    수정
                </Button>
            </div>
        </Container>
    );
}
