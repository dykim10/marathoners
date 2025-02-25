"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { Form, Button, Alert, Container, Row, Col } from "react-bootstrap";

export default function RegisterPage() {
    const router = useRouter();

    // 입력값 상태 관리
    const [formData, setFormData] = useState({
        userId: "",
        userEmail: "",
        userPassword: "",
        userName: "",
    });

    // 검증 상태 관리
    const [validationErrors, setValidationErrors] = useState({});
    const [checkMessage, setCheckMessage] = useState({ userId: null, userEmail: null });
    const [isChecked, setIsChecked] = useState({ userId: false, userEmail: false });

    // 입력값 변경 핸들러
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });

        // 입력값이 변경되면 중복 확인 상태 초기화
        setIsChecked((prev) => ({ ...prev, [name]: false }));
        setCheckMessage((prev) => ({ ...prev, [name]: null }));
    };

    const checkDuplicate = async (type) => {
        const value = type === "userId" ? formData.userId : formData.userEmail;

        if (!value) {
            alert("값을 입력해주세요.");
            return "UNKNOWN";
        }

        try {
            const response = await fetch("/api/check-user", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ type, value }),
            });

            const data = await response.json();
            const userExistsStatus = data.userExists || "UNKNOWN";
            console.log(`${type} 중복 확인 결과:`, userExistsStatus);

            const existsMessage = (userExistsStatus === "USERID_EXISTS" || userExistsStatus === "EMAIL_EXISTS")
                ? "이미 사용 중입니다."
                : "사용 가능합니다.";

            setCheckMessage((prev) => ({ ...prev, [type]: existsMessage }));
            setIsChecked((prev) => ({ ...prev, [type]: userExistsStatus !== "USERID_EXISTS" && userExistsStatus !== "EMAIL_EXISTS" }));

            return userExistsStatus;  //중복 확인 결과를 반환

        } catch (error) {
            console.error(`${type} 중복 확인 실패:`, error);
            setCheckMessage((prev) => ({ ...prev, [type]: "서버 오류가 발생했습니다." }));
            return "ERROR";
        }
    };


    const handleSubmit = async (e) => {
        e.preventDefault();
        let errors = {};

        // 유효성 검사
        if (!/^[a-zA-Z0-9]{4,16}$/.test(formData.userId)) errors.userId = "아이디는 4~16자 영어 또는 숫자만 입력 가능합니다.";
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.userEmail)) errors.userEmail = "이메일 형식이 올바르지 않습니다.";
        if (!/^(?=.*[a-zA-Z])(?=.*[\W_]).{8,}$/.test(formData.userPassword)) {
            errors.userPassword = "비밀번호는 영문자와 특수문자를 포함하여 8자 이상이어야 합니다.";
        }

        if (Object.keys(errors).length > 0) {
            setValidationErrors(errors);
            return;
        }

        // 최종 중복 확인 (isChecked가 false면 API 재호출)
        const idCheck = isChecked.userId ? "NOT_FOUND" : await checkDuplicate("userId");
        const emailCheck = isChecked.userEmail ? "NOT_FOUND" : await checkDuplicate("userEmail");

        console.log("최종 idCheck 결과:", idCheck); // 디버깅용 로그

        if (idCheck === "USERID_EXISTS") {
            setValidationErrors((prev) => ({ ...prev, userId: "이미 사용 중인 아이디입니다." }));
            return;
        }

        if (emailCheck === "EMAIL_EXISTS") {
            setValidationErrors((prev) => ({ ...prev, userEmail: "이미 등록된 이메일입니다." }));
            return;
        }

        // 회원가입 요청
        try {
            const response = await fetch("/api/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                router.push("/auth/login");
            } else {
                setValidationErrors({ form: "회원가입 실패! 다시 시도해주세요." });
            }
        } catch (error) {
            console.error("회원가입 오류:", error);
            setValidationErrors({ form: "서버 오류가 발생했습니다." });
        }
    };

    return (
        <Container className="mt-4">
            <h2 className="text-center mb-4">회원가입</h2>

            <Form onSubmit={handleSubmit} autoComplete="off">  {/* 전체 폼 자동완성 비활성화 */}

                {/* 아이디 입력 (3등분 레이아웃) */}
                <Form.Group className="mb-3">
                    <Row className="align-items-center">
                        <Col xs={3}>
                            <Form.Label>아이디</Form.Label>
                        </Col>
                        <Col xs={6}>
                            <Form.Control
                                type="text"
                                name="userId"
                                value={formData.userId}
                                onChange={handleChange}
                                placeholder="아이디 입력 (4~16자)"
                                autoComplete="off"
                                required
                            />
                        </Col>
                        <Col xs={3}>
                            <Button variant="outline-secondary" size="sm" onClick={() => checkDuplicate("userId")}>
                                중복 확인
                            </Button>
                        </Col>
                    </Row>
                    {checkMessage.userId && <div className={`mt-2 ${checkMessage.userId.includes("가능") ? "text-green-500" : "text-red-500"}`}>{checkMessage.userId}</div>}
                </Form.Group>

                {/* 이메일 입력 (3등분 레이아웃) */}
                <Form.Group className="mb-6">
                    <Row className="align-items-center">
                        <Col xs={3}>
                            <Form.Label>이메일</Form.Label>
                        </Col>
                        <Col xs={6}>
                            <Form.Control
                                type="email"
                                name="userEmail"
                                value={formData.userEmail}
                                onChange={handleChange}
                                placeholder="이메일 입력"
                                autoComplete="off"
                                required
                            />
                        </Col>
                        <Col xs={3}>
                            <Button variant="outline-secondary" size="sm" onClick={() => checkDuplicate("userEmail")}>
                                중복 확인
                            </Button>
                        </Col>
                    </Row>
                    {checkMessage.userEmail && <div className={`mt-2 ${checkMessage.userEmail.includes("가능") ? "text-green-500" : "text-red-500"}`}>{checkMessage.userEmail}</div>}
                </Form.Group>

                {/* 비밀번호 입력 (라벨 + input 레이아웃 적용) */}
                <Form.Group className="mb-6">
                    <Row className="align-items-center">
                        <Col xs={3}>
                            <Form.Label>비밀번호</Form.Label>
                        </Col>
                        <Col xs={4}>
                            <Form.Control
                                type="password"
                                name="userPassword"
                                value={formData.userPassword}
                                onChange={handleChange}
                                placeholder="대소문자, 특수문자 포함 8자 이상"
                                autoComplete="new-password"  //비밀번호 자동완성 방지
                                required
                            />
                        </Col>
                    </Row>
                </Form.Group>

                {/* 이름 입력 (라벨 + input 레이아웃 적용) */}
                <Form.Group className="mb-6">
                    <Row className="align-items-center">
                        <Col xs={3}>
                            <Form.Label>이름</Form.Label>
                        </Col>
                        <Col xs={4}>
                            <Form.Control
                                type="text"
                                name="userName"
                                value={formData.userName}
                                onChange={handleChange}
                                placeholder="이름을 입력해주세요. "
                                autoComplete="off"
                                required
                            />
                        </Col>
                    </Row>
                </Form.Group>

                {/* 오류 메시지 출력 */}
                {Object.values(validationErrors).map((error, index) => (
                    <Alert key={index} variant="danger">{error}</Alert>
                ))}

                {/* 회원가입 버튼 */}
                <Button variant="primary" type="submit" className="w-100">회원가입</Button>
            </Form>
        </Container>
    );
}
