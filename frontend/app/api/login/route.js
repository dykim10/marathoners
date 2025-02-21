export async function POST(req) {
    try {
        const { userId, password } = await req.json();
        console.log("🔹 Next.js API Route에서 Spring Boot 요청 시도:", { userId, password });
        const API_URL = process.env.NEXT_PUBLIC_API_URL; // Spring Boot 서버 URL

        const response = await fetch(`${API_URL}/api/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
            },
            credentials: "include",  // 세션 유지
            body: JSON.stringify({ userId, password }),
        });

        console.log("🔹 Spring Boot 응답 상태 코드:", response.status);

        if (!response.ok) {
            const errorText = await response.text();
            console.log("❌ Spring Boot 응답 내용:", errorText);
            return Response.json({ error: "로그인 실패", detail: errorText }, { status: response.status });
        }

        const data = await response.json();
        return Response.json(data);
    } catch (error) {
        console.error("❌ Next.js API Route 내부 오류:", error);
        return Response.json({ error: "서버 오류 발생" }, { status: 500 });
    }
}
