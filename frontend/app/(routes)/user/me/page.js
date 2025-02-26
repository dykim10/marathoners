/**
 * 사용자가 본인 정보 보는 페이지.
 */
"use client";

import { useEffect, useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

export default function UserProfilePage() {
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

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
        return <div className="flex justify-center items-center h-screen text-lg font-semibold">⏳ 로딩 중...</div>;

    if (error)
        return <div className="flex justify-center items-center h-screen text-red-600 font-semibold">{error}</div>;

    return (
        <div className="flex justify-center mt-8">
            <UserInfoCard userData={userData} />
        </div>
    );
}

function UserInfoCard({ userData }) {
    return (
        <Card className="w-96 shadow-lg">
            <CardHeader>
                <CardTitle className="text-xl text-center">내 정보</CardTitle>
            </CardHeader>
            <CardContent>
                <div className="space-y-3 text-gray-700">
                    <InfoRow label="아이디" value={userData.userId} />
                    <InfoRow label="이메일" value={userData.userEmail} />
                    <InfoRow label="이름" value={userData.userName} />
                </div>
            </CardContent>
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
