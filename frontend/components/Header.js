"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { checkSession } from "@/utils/session";
import { Navbar, Nav, NavDropdown, Container, Button } from "react-bootstrap";
import Link from "next/link";

export default function Header() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [user, setUser] = useState(null);

    const router = useRouter();

    useEffect(() => {
        const verifySession = async () => {
            const sessionExists = await checkSession();
            setIsLoggedIn(sessionData.success);
            setUser(sessionData.user);

        };
        verifySession();
    }, []);

    const handleMyInfo = () => {
        router.push("/user/me");
    };

    const handleLogout = async () => {
        try {
            await fetch("/api/logout", {
                method: "POST",
                credentials: "include",
            });
            setIsLoggedIn(sessionData.success);
            window.location.href = "/";
        } catch (error) {
            console.error("로그아웃 실패:", error);
        }
    };

    return (
        <Navbar bg="dark" variant="dark" expand="lg" className="py-3 shadow-sm">
            <Container>
                {/* 좌측: 로고 및 메뉴 */}
                <Navbar.Brand as={Link} href="/" className="fw-bold text-light me-3">
                    Marathoners
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <NavDropdown title="메뉴1" id="menu1-dropdown">
                            <NavDropdown.Item as={Link} href="/menu1/sub1">서브메뉴1</NavDropdown.Item>
                            <NavDropdown.Item as={Link} href="/menu1/sub2">서브메뉴2</NavDropdown.Item>
                        </NavDropdown>
                        <NavDropdown title="메뉴2" id="menu2-dropdown">
                            <NavDropdown.Item as={Link} href="/menu2/sub1">서브메뉴1</NavDropdown.Item>
                            <NavDropdown.Item as={Link} href="/menu2/sub2">서브메뉴2</NavDropdown.Item>
                        </NavDropdown>
                        <NavDropdown title="메뉴3" id="menu3-dropdown">
                            <NavDropdown.Item as={Link} href="/menu3/sub1">서브메뉴1</NavDropdown.Item>
                            <NavDropdown.Item as={Link} href="/menu3/sub2">서브메뉴2</NavDropdown.Item>
                        </NavDropdown>
                        <NavDropdown title="메뉴4" id="menu4-dropdown">
                            <NavDropdown.Item as={Link} href="/menu4/sub1">서브메뉴1</NavDropdown.Item>
                            <NavDropdown.Item as={Link} href="/menu4/sub2">서브메뉴2</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>

                    {/* 우측: 로그인 상태 관리 */}
                    <Nav className="ms-auto">
                        {isLoggedIn ? (
                            <>
                                <Button className="btn btn-danger btn-sm me-2" variant="danger" onClick={handleLogout}>
                                    로그아웃
                                </Button>
                                <Button className="btn btn-warning btn-sm" variant="warning" onClick={handleMyInfo}>
                                    내 정보
                                </Button>
                            </>
                        ) : (
                            <Nav.Link as={Link} href="/auth/login" className="text-light">
                                로그인
                            </Nav.Link>
                        )}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}
