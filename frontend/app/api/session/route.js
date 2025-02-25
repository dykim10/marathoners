export async function GET(request) { // `context`에서 `request`를 추출해야 함
    try {

        const cookieHeader = request.headers.get("cookie"); // 요청 헤더에서 쿠키 가져오기
        console.log("요청된 쿠키:", cookieHeader);

        const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/session`, {
            method: "GET",
            credentials: "include",
            headers: {
                Cookie: cookieHeader || "", // 백엔드로 쿠키 전달
            },
        });

        console.log("`/api/session` 요청 Headers:", response.headers);
        console.log("백엔드 `/api/session` 응답 상태:", response.status);

        if (response.status === 401) {
            console.log("로그인되지 않은 상태 - 세션 없음 (Next.js)");
            return new Response(JSON.stringify({ message: "로그인되지 않은 상태" }), { status: 200 });
        }

        if (!response.ok) {
            console.log("백엔드 `/api/session` 요청 실패 - 상태 코드:", response.status);
            return new Response(JSON.stringify({ error: "세션 없음" }), { status: response.status });
        }

        const data = await response.json();
        return new Response(JSON.stringify(data), {
            status: 200,
            headers: {
                "Content-Type": "application/json",
            },
        });
    } catch (error) {
        console.error("Next.js API Route 내부 오류:", error);
        return new Response(JSON.stringify({ error: "서버 오류 발생" }), { status: 500 });
    }
}
