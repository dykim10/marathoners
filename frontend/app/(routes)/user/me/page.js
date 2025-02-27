/**
 * 사용자가 본인 정보 보는 페이지.
 */
"use client";

import { useEffect, useState } from "react";
import { Button, Card } from 'react-bootstrap';
import { useRouter } from "next/navigation";

export default function UserProfilePage() {
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const router = useRouter();
    
    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await fetch("/api/user/");
                const data = await response.json();
                if (data.error) throw new Error(data.error);
                setUserData(data);
            } catch (err) {
                console.error("내 정보 조회 오류:", err);
                setError("정보를 불러오는 중 오류가 발생했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchUserData();
    }, []);

    if (loading)
        return <div className="flex justify-center items-center h-screen text-lg font-semibold">로딩 중...</div>;

    if (error)
        return <div className="flex justify-center items-center h-screen text-red-600 font-semibold">{error}</div>;

    return (
        <div className="flex flex-col items-center mt-8 space-y-4">
            <UserInfoCard userData={userData} />
            <Button onClick={() => router.push("/user/modify")} className="w-40">
                내 정보 수정하기
            </Button>
        </div>
    );
}

function UserInfoCard({ userData }) {
    return (
        <Card style={{ width: "24rem" }} className="shadow-sm">
            <Card.Header className="text-center fw-bold">내 정보</Card.Header>
            <Card.Body>
                <InfoRow label="아이디" value={userData.userId} />
                <InfoRow label="이메일" value={userData.userEmail} />
                <InfoRow label="이름" value={userData.userName} />
            </Card.Body>
        </Card>
    );
}

function InfoRow({ label, value }) {
    return (
        <p>
            <strong className="text-gray-900">{label}:</strong> {value}
        </p>
    );
}
