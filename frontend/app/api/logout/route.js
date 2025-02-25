export async function POST() {
    try {
        const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/logout`, {
            method: "POST",
            credentials: "include",
        });

        if (!response.ok) {
            return new Response(JSON.stringify({ error: "로그아웃 실패" }), { status: response.status });
        }

        return new Response(JSON.stringify({ message: "로그아웃 성공" }), { status: 200 });
    } catch (error) {
        console.error("로그아웃 오류:", error);
        return new Response(JSON.stringify({ error: "서버 오류 발생" }), { status: 500 });
    }
}
