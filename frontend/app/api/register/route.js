export async function POST(request) {
    try {
        // ✅ 클라이언트에서 보낸 회원가입 데이터 파싱
        const formData = await request.json();
        const API_URL = process.env.NEXT_PUBLIC_API_URL; // Spring Boot 서버 주소

        console.log("회원가입 요청 데이터:", formData);
        return;

        // ✅ 백엔드 UserController (`/api/register`)로 요청 전달
        const response = await fetch(`${API_URL}/api/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(formData),
        });

        if (!response.ok) {
            throw new Error(`서버 응답 오류! 상태 코드: ${response.status}`);
        }

        // ✅ 백엔드 응답 데이터 파싱
        const data = await response.json();
        return new Response(JSON.stringify(data), { status: 200 });

    } catch (error) {
        console.error("Next.js API Route 내부 오류:", error);
        return new Response(JSON.stringify({ error: "서버 오류 발생" }), { status: 500 });
    }
}
