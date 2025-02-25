export const checkSession = async () => {
    try {
        const response = await fetch("/api/session", {
            method: "GET",
            credentials: "include", // 🔹 세션 쿠키 포함 요청
        });

        console.log("`/api/session` 응답 상태:", response.status);

        if (response.status === 401) {
            console.log("로그인되지 않은 상태 - 세션 없음");
            return false; // 세션 없음
        }

        if (!response.ok) {
            throw new Error("세션 확인 실패");
        }

        const data = await response.json();
        console.log("세션 유지됨:", data);
        return true; // 세션 유지됨
    } catch (error) {
        console.error("세션 확인 중 오류 발생:", error);
        return false;
    }
};
