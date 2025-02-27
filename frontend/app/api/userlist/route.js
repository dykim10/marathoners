export async function POST(request) {
    try {
        const { keyword, page, rows } = await request.json();
        const API_URL = process.env.NEXT_PUBLIC_API_URL;

        // Spring Boot API 호출
        const response = await fetch(`${API_URL}/api/userlist`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                keyword,
                page,
                rows
            }),
        });
        console.log("2-1");
        if (!response.ok) {
            console.log("2-222");
            throw new Error("Failed to fetch data from backend");
        }

        const data = await response.json();
        return new Response(JSON.stringify(data), { status: 200 });

    } catch (error) {
        //return NextResponse.json({ error: "Failed to fetch users" }, { status: 500 });
        console.error("111 Failed to fetch users : ", error);
        return new Response(JSON.stringify({ error: "23242 Failed to fetch users" }), { status: 500 });
    }
}
