"use client";

import { useState, useRef, useEffect } from "react";
import { Form, Button, Container, Row, Col } from "react-bootstrap";
import dynamic from "next/dynamic";
import "react-datepicker/dist/react-datepicker.css";

const DatePicker = dynamic(() => import("react-datepicker").then((mod) => mod.default), { ssr: false });

export const RACE_TYPES = [
    { key: "WALK_COURSE", label: "걷기" },
    { key: "FIVE_COURSE", label: "5K" },
    { key: "TEN_COURSE", label: "10K" },
    { key: "HALF_COURSE", label: "Half" },
    { key: "FULL_COURSE", label: "풀코스" },
    { key: "FIFTY_COURSE", label: "50K" },
    { key: "HUNDRED_COURSE", label: "100K" },
    { key: "ETC_COURSE", label: "기타" },
];

export default function MarathonRegister({ initialData = null }) {
    const [formData, setFormData] = useState({
        mrName: initialData?.mrName || "",
        mrStartDt: initialData?.mrStartDt ? new Date(initialData.mrStartDt) : new Date(),
        mrLocation: initialData?.mrLocation || "",
        mrCompany: initialData?.mrCompany || "",
        mrContent: initialData?.mrContent || "",
        mrHomepageUrl: initialData?.mrHomepageUrl || "",
        selectedRaceTypes: initialData?.selectedRaceTypes || [],
        raceDetails: initialData?.raceDetails || {},
    });

    // ✅ 날짜 선택 핸들러
    const handleDateChange = (date) => {
        setFormData((prev) => ({ ...prev, mrStartDt: date }));
    };

    // ✅ 대회 유형 체크박스 변경 핸들러 (다중 선택 유지)
    const handleRaceTypeChange = (key) => {
        setFormData((prev) => {
            const updatedRaceTypes = prev.selectedRaceTypes.includes(key)
                ? prev.selectedRaceTypes.filter((type) => type !== key)
                : [...prev.selectedRaceTypes, key];

            return {
                ...prev,
                selectedRaceTypes: updatedRaceTypes,
                raceDetails: {
                    ...prev.raceDetails,
                    [key]: prev.raceDetails[key] || { price: "", capacity: "", etc: "" },
                },
            };
        });
    };

    // ✅ 입력 필드 값 변경 핸들러
    const handleInputChange = (key, field, value) => {
        setFormData((prev) => ({
            ...prev,
            raceDetails: {
                ...prev.raceDetails,
                [key]: { ...prev.raceDetails[key], [field]: value },
            },
        }));
    };

    // ✅ 제출 핸들러
    const handleSubmit = (e) => {
        e.preventDefault();
        if (!formData.mrName.trim()) return alert("대회명을 입력하세요.");
        if (!formData.mrStartDt) return alert("날짜를 선택하세요.");
        if (!formData.mrLocation.trim()) return alert("위치를 입력하세요.");
        if (!formData.mrContent.trim()) return alert("대회 설명을 입력하세요.");
        if (formData.selectedRaceTypes.length === 0) return alert("대회 유형을 하나 이상 선택하세요.");

        console.log("제출 데이터:", formData);
        // TODO: API 요청 로직 추가
    };

    return (
        <Container className="mt-5">
            <Row className="justify-content-md-center">
                <Col md={8}>
                    <h2 className="text-center">대회 등록</h2>
                    <Form onSubmit={handleSubmit}>
                        {/* 대회명 */}
                        <Form.Group className="mb-3">
                            <Form.Label>대회명</Form.Label>
                            <Form.Control type="text" value={formData.mrName} onChange={(e) => setFormData({ ...formData, mrName: e.target.value })} required />
                        </Form.Group>

                        {/* 대회 날짜 */}
                        <Form.Group className="mb-3">
                            <Row className="align-items-center">
                                <Col xs="auto">
                                    <Form.Label className="m-0">대회 날짜</Form.Label>
                                </Col>
                                <Col>
                                    <DatePicker selected={formData.mrStartDt} onChange={handleDateChange} dateFormat="yyyy-MM-dd" className="form-control" />
                                </Col>
                            </Row>
                        </Form.Group>

                        {/* 대회 장소   */}
                        <Form.Group className="mb-3">
                            <Form.Label>대회 장소</Form.Label>
                            <Form.Control type="text" value={formData.mrLocation} onChange={(e) => setFormData({ ...formData, mrLocation: e.target.value })} required />
                        </Form.Group>

                        {/* 대회 주관사   */}
                        <Form.Group className="mb-3">
                            <Form.Label>대회 주관사</Form.Label>
                            <Form.Control type="text" value={formData.mrCompany} onChange={(e) => setFormData({ ...formData, mrCompany: e.target.value })} required />
                        </Form.Group>

                        {/* 대회 홈페이지   */}
                        <Form.Group className="mb-3">
                            <Form.Label>대회 홈페이지</Form.Label>
                            <Form.Control type="text" value={formData.mrHomepageUrl} onChange={(e) => setFormData({ ...formData, mrHomepageUrl: e.target.value })} required />
                        </Form.Group>
                        
                        {/* 대회 유형 선택 (체크박스 + 입력 필드 한 줄 배치) */}
                        <Form.Group className="mb-3">
                            <Form.Label>대회 유형</Form.Label>
                            {RACE_TYPES.map(({ key, label }) => (
                                <Row key={key} className="align-items-center mb-2">
                                    <Col xs="auto">
                                        <Form.Check type="checkbox" label={label} checked={formData.selectedRaceTypes.includes(key)} onChange={() => handleRaceTypeChange(key)} />
                                    </Col>
                                    {formData.selectedRaceTypes.includes(key) && (
                                        <>
                                            <Col>
                                                <Form.Control type="number" placeholder="가격" value={formData.raceDetails[key]?.price || ""} onChange={(e) => handleInputChange(key, "price", e.target.value)} />
                                            </Col>
                                            <Col>
                                                <Form.Control type="number" placeholder="모집 인원" value={formData.raceDetails[key]?.capacity || ""} onChange={(e) => handleInputChange(key, "capacity", e.target.value)} />
                                            </Col>
                                            {key === "ETC_COURSE" && (
                                                <Col>
                                                    <Form.Control type="text" placeholder="기타 내용" value={formData.raceDetails[key]?.etc || ""} onChange={(e) => handleInputChange(key, "etc", e.target.value)} />
                                                </Col>
                                            )}
                                        </>
                                    )}
                                </Row>
                            ))}
                        </Form.Group>

                        {/* 설명 입력 (Toast UI Editor) */}
                        <Form.Group className="mb-3">
                            <Form.Label>대회 설명</Form.Label>
                            <Form.Control as="textarea" rows={5} value={formData.mrContent} onChange={(e) => setFormData({ ...formData, mrContent: e.target.value })} required />
                        </Form.Group>

                        <Button variant="primary" type="submit" className="w-100">
                            {initialData ? "수정하기" : "등록하기"}
                        </Button>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
}
