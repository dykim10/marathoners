import { NextResponse } from "next/server";

export async function GET(req, { params }) {
    const { mrUuid } = params;

    if (!mrUuid) {
        return NextResponse.json({ error: "Invalid request: mrUuid is required" }, { status: 400 });
    }

    try {
        const API_URL = process.env.NEXT_PUBLIC_API_URL; // 백엔드 API URL
        const response = await fetch(`${API_URL}/api/marathon/detail/${mrUuid}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            return NextResponse.json({ error: "Failed to fetch race details" }, { status: response.status });
        }

        const data = await response.json();
        return NextResponse.json(data, { status: 200 });

    } catch (error) {
        console.error("Error fetching race details:", error);
        return NextResponse.json({ error: "Internal Server Error" }, { status: 500 });
    }
}
