"use client";

import { useState, useRef, useEffect } from "react";
import { Form, Button, Container, Row, Col } from "react-bootstrap";
import { Editor } from "@toast-ui/react-editor";
import "@toast-ui/editor/dist/toastui-editor.css"; // Toast UI Editor 기본 스타일
import "@toast-ui/editor/dist/theme/toastui-editor-dark.css"; // 다크 테마 지원

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
        mrStartDt: initialData?.mrStartDt || "",
        mrLocation: initialData?.mrLocation || "",
        mrCompany: initialData?.mrCompany || "",
        mrContent: initialData?.mrContent || "",
        mrHomepageUrl: initialData?.mrHompageUrl || "",

        //대회의 코스 디테일설정
        selectedRaceTypes: initialData?.selectedRaceTypes || [],
        raceDetails: initialData?.raceDetails || {}, // { 유형: { price: "", capacity: "", etc: "" } }
    });

    const editorRef = useRef(null);

    useEffect(() => {
        if (initialData && editorRef.current) {
            editorRef.current.getInstance().setMarkdown(initialData.description || "");
        }
    }, [initialData]);

    // 입력 필드 값 변경 처리
    const handleInputChange = (key, field, value) => {
        setFormData((prev) => ({
            ...prev,
            raceDetails: {
                ...prev.raceDetails,
                [key]: { ...prev.raceDetails[key], [field]: value },
            },
        }));
    };

    // 에디터 내용 변경 시 formData에 저장
    const handleEditorChange = () => {
        const editorInstance = editorRef.current.getInstance();
        const content = editorInstance.getMarkdown();
        setFormData((prev) => ({ ...prev, description: content }));
    };

    // 대회 코스 유형 체크박스 선택/해제 처리
    const handleRaceTypeChange = (key) => {
        setFormData((prev) => {
            const updatedRaceTypes = prev.selectedRaceTypes.includes(key)
                ? prev.selectedRaceTypes.filter((type) => type !== key)
                : [...prev.selectedRaceTypes, key];

            const updatedRaceDetails = { ...prev.raceDetails };

            // 선택 해제 시 해당 유형의 입력 필드 값 초기화
            if (!updatedRaceTypes.includes(key)) {
                delete updatedRaceDetails[key];
            } else {
                updatedRaceDetails[key] = { price: "", capacity: "", etc: "" };
            }

            return { ...prev, selectedRaceTypes: updatedRaceTypes, raceDetails: updatedRaceDetails };
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // 에디터 내용 가져오기
        const description = editorRef.current.getInstance().getMarkdown();

        // 필수값 검증
        if (!formData.mrName.trim()) return alert("대회명을 입력하세요.");
        if (!formData.mrStartDt) return alert("날짜를 선택하세요.");
        if (!formData.mrLocation.trim()) return alert("위치를 입력하세요.");
        if (!formData.mrContent.trim()) return alert("대회 설명을 입력하세요.");
        //if (!formData.mrHomepageUrl.trim()) return alert("홈페이지를 입력하세요.");
        //if (!formData.mrCompany.trim()) return alert("주최/주관사를 입력하세요.");

        if (formData.selectedRaceTypes.length === 0) return alert("대회 유형을 하나 이상 선택하세요.");
        const payload = { ...formData, description };

        console.log("🚀 제출 데이터:", payload);
        // TODO: API 요청 로직 추가
    };

    return (
        <Container className="mt-5">
            <Row className="justify-content-md-center">
                <Col md={8}>
                    <h2 className="text-center">🏃‍♂️ 마라톤 대회 {initialData ? "수정" : "등록"}</h2>
                    <Form onSubmit={handleSubmit}>
                        {/* 대회명 입력 */}
                        <Form.Group className="mb-3">
                            <Form.Label>대회명</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="대회명을 입력하세요"
                                name="raceName"
                                value={formData.mrRaceName}
                                onChange={handleInputChange}
                                required
                            />
                        </Form.Group>

                        {/* 날짜 선택 */}
                        <Form.Group className="mb-3">
                            <Form.Label>대회날짜</Form.Label>
                            <Form.Control
                                type="text"
                                name="startDate"
                                value={formData.mrStartDt}
                                onChange={handleInputChange}
                                required
                            />
                        </Form.Group>

                        {/* 위치 입력 */}
                        <Form.Group className="mb-3">
                            <Form.Label>위치</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="위치를 입력하세요"
                                name="location"
                                value={formData.location}
                                onChange={handleInputChange}
                                required
                            />
                        </Form.Group>

                        {/* 선택된 대회 유형별 입력 필드 */}
                        {formData.selectedRaceTypes.map((key) => (
                            <div key={key} className="mb-3 p-3 border rounded bg-light">
                                <h5>{RACE_TYPES.find((r) => r.key === key)?.label} 설정</h5>
                                <Form.Group className="mb-2">
                                    <Form.Label>가격</Form.Label>
                                    <Form.Control type="number" value={formData.raceDetails[key]?.price || ""} onChange={(e) => handleInputChange(key, "price", e.target.value)} placeholder="가격 입력" />
                                </Form.Group>

                                <Form.Group className="mb-2">
                                    <Form.Label>모집 인원</Form.Label>
                                    <Form.Control type="number" value={formData.raceDetails[key]?.capacity || ""} onChange={(e) => handleInputChange(key, "capacity", e.target.value)} placeholder="모집 인원 입력" />
                                </Form.Group>

                                {/* "기타" 선택 시만 표시 */}
                                {key === "ETC_COURSE" && (
                                    <Form.Group className="mb-2">
                                        <Form.Label>기타 내용</Form.Label>
                                        <Form.Control type="text" value={formData.raceDetails[key]?.etc || ""} onChange={(e) => handleInputChange(key, "etc", e.target.value)} placeholder="기타 내용을 입력하세요" />
                                    </Form.Group>
                                )}
                            </div>
                        ))}

                        {/* 주관사 입력 */}
                        <Form.Group className="mb-3">
                            <Form.Label>주최</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="주관사를 입력하세요"
                                name="mr_company"
                                value={formData.mrCompany}
                                onChange={handleInputChange}
                                required
                            />
                        </Form.Group>

                        {/* 홈페이지 입력 */}
                        <Form.Group className="mb-3">
                            <Form.Label>위치</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="URL 입력하세요"
                                name="mrHomepageUrl"
                                value={formData.mrHomepageUrl}
                                onChange={handleInputChange}
                            />
                        </Form.Group>
                        
                        {/* 설명 입력 (Toast UI Editor) */}
                        <Form.Group className="mb-3">
                            <Form.Label>대회 설명</Form.Label>
                            <Editor previewStyle="vertical"
                                    height="300px"
                                    initialEditType="wysiwyg"
                                    useCommandShortcut={true}
                                    theme="dark"
                                    ref={editorRef}
                                    onChange={handleEditorChange} />
                        </Form.Group>

                        {/* 등록 또는 수정 버튼 */}
                        <Button variant="primary" type="submit" className="w-100">
                            {initialData ? "수정하기" : "등록하기"}
                        </Button>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
}
