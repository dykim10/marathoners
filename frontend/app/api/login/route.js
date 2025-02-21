/*
    Next.js에서 API 요청을 받아 Spring Boot로 프록시
    CORS 문제 없이 Next.js가 백엔드와 통신
    fetch("/api/login")이 Next.js 내부 API로 연결됨
*/
export async function POST(req) {
    const { userId, password } = await req.json();

    const API_URL = process.env.NEXT_PUBLIC_API_URL; // Spring Boot API URL
    const response = await fetch(`${API_URL}/api/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
        },
        credentials: "include",  // 세션 유지
        body: JSON.stringify({ userId, password }),
    });

    if (!response.ok) {
        return Response.json({ error: "로그인 실패" }, { status: response.status });
    }

    const data = await response.json();
    return Response.json(data);
}
