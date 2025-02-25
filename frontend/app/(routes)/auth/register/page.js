"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Form, Button, Alert, Container, Row, Col } from "react-bootstrap";

export default function RegisterPage() {
    const router = useRouter();

    // ✅ 입력값 상태 관리
    const [formData, setFormData] = useState({
        userId: "",
        userEmail: "",
        userPassword: "",
        userName: "",
    });

    // ✅ 검증 결과 상태 관리
    const [validationErrors, setValidationErrors] = useState({});
    const [idCheckMessage, setIdCheckMessage] = useState(null);
    const [emailCheckMessage, setEmailCheckMessage] = useState(null);

    // ✅ 입력값 변경 핸들러
    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    // ✅ 아이디 중복 확인 (서버 요청)
    const checkUsername = async () => {
        if (!validateUsername(formData.userId)) {
            setIdCheckMessage("아이디는 4~16자 사이여야 합니다.");
            return;
        }

        try {
            const response = await fetch("/api/check-username", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ userId: formData.userId }),
            });

            const data = await response.json();
            setIdCheckMessage(data.exists ? "이미 사용 중인 아이디입니다." : "사용 가능한 아이디입니다.");
        } catch (error) {
            console.error("아이디 중복 확인 실패:", error);
            setIdCheckMessage("⚠ 서버 오류가 발생했습니다.");
        }
    };

    // ✅ 이메일 중복 확인 (서버 요청)
    const checkEmail = async () => {
        if (!validateEmail(formData.userEmail)) {
            setEmailCheckMessage("올바른 이메일 형식이 아닙니다.");
            return;
        }

        try {
            const response = await fetch("/api/check-email", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ userEmail: formData.userEmail }),
            });

            const data = await response.json();
            setEmailCheckMessage(data.exists ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.");
        } catch (error) {
            console.error("이메일 중복 확인 실패:", error);
            setEmailCheckMessage("⚠ 서버 오류가 발생했습니다.");
        }
    };

    // ✅ 폼 검증 함수
    const validateUsername = (userId) => /^[a-zA-Z0-9]{4,16}$/.test(userId);
    const validateEmail = (userEmail) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(userEmail);
    const validatePassword = (userPassword) => /^(?=.*[a-z])(?=.*[A-Z])(?=.*[\W_]).{8,}$/.test(userPassword);

    // ✅ 회원가입 처리
    const handleSubmit = async (e) => {
        e.preventDefault();
        let errors = {};

        if (!validateUsername(formData.userId)) errors.userId = "아이디는 4~16자 영어 또는 숫자만 입력 가능합니다.";
        if (!validateEmail(formData.userEmail)) errors.userEmail = "이메일 형식이 올바르지 않습니다.";
        if (!validatePassword(formData.userPassword)) errors.userPassword = "비밀번호는 대소문자, 특수문자를 포함하여 8자 이상이어야 합니다.";

        if (Object.keys(errors).length > 0) {
            setValidationErrors(errors);
            return;
        }

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
            setValidationErrors({ form: "⚠ 서버 오류가 발생했습니다." });
        }
    };

    return (
        <Container className="mt-4">
            <h2 className="text-center mb-4">회원가입</h2>

            <Form onSubmit={handleSubmit} autoComplete="off">  {/* 전체 폼 자동완성 비활성화 */}

                {/* ✅ 아이디 입력 (3등분 레이아웃) */}
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
                            <Button variant="outline-secondary" size="sm" onClick={checkUsername}>
                                중복 확인
                            </Button>
                        </Col>
                    </Row>
                    {idCheckMessage && <div className="mt-2">{idCheckMessage}</div>}
                </Form.Group>

                {/* ✅ 이메일 입력 (3등분 레이아웃) */}
                <Form.Group className="mb-3">
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
                            <Button variant="outline-secondary" size="sm" onClick={checkEmail}>
                                중복 확인
                            </Button>
                        </Col>
                    </Row>
                    {emailCheckMessage && <div className="mt-2">{emailCheckMessage}</div>}
                </Form.Group>

                {/* ✅ 비밀번호 입력 (라벨 + input 레이아웃 적용) */}
                <Form.Group className="mb-3">
                    <Row className="align-items-center">
                        <Col xs={3}>
                            <Form.Label>비밀번호</Form.Label>
                        </Col>
                        <Col xs={9}>
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

                {/* ✅ 이름 입력 (라벨 + input 레이아웃 적용) */}
                <Form.Group className="mb-3">
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

                {/* ✅ 오류 메시지 출력 */}
                {Object.values(validationErrors).map((error, index) => (
                    <Alert key={index} variant="danger">{error}</Alert>
                ))}

                {/* ✅ 회원가입 버튼 */}
                <Button variant="primary" type="submit" className="w-100">회원가입</Button>
            </Form>
        </Container>
    );
}
