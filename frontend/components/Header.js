"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { checkSession } from "@/utils/session"; // 세션 확인 함수
import { Navbar, Nav, Container, Button } from "react-bootstrap";

export default function Header() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const router = useRouter();

    useEffect(() => {
        const verifySession = async () => {
            const sessionExists = await checkSession();
            setIsLoggedIn(sessionExists);
        };
        verifySession();
    }, []);

    const handleLogout = async () => {
        try {
            await fetch("/api/logout", {
                method: "POST",
                credentials: "include",
            });

            setIsLoggedIn(false);
            //router.push("/"); // 로그아웃 후 메인 페이지로 이동 (CSR 방식이라는 점을 알아둬야한다.)
            window.location.href = "/";

        } catch (error) {
            console.error(" 로그아웃 실패:", error);
        }
    };

    return (
        <Navbar bg="dark" variant="dark" expand="lg">
            <Container>
                <Navbar.Brand href="/">Marathoners</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="ms-auto">
                        <Nav.Link href="/">Home</Nav.Link>
                        {isLoggedIn ? (
                            <Button variant="danger" className="btn btn-danger btn-sm" onClick={handleLogout}>Logout</Button>
                        ) : (
                            <Nav.Link href="/auth/login">Login</Nav.Link>
                        )}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}
