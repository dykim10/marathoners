"use client";

import UserForm from "@/components/UserForm";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function EditProfilePage() {
    const [userData, setUserData] = useState(null);
    const router = useRouter();

    useEffect(() => {
        fetch("/api/user/me")
            .then((res) => res.json())
            .then((data) => setUserData(data));
    }, []);

    const handleUpdate = async (formData) => {
        const response = await fetch("/api/user/", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(formData),
        });

        if (response.ok) {
            alert("정보가 수정되었습니다.");
            router.push("/user/profile");
        } else {
            alert("정보 수정 실패");
        }
    };

    return userData ? <UserForm initialData={userData} onSubmit={handleUpdate} isEdit={true} /> : <p>로딩 중...</p>;
}
