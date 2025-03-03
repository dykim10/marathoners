import NextAuth from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";

export const authOptions = {
    providers: [
        CredentialsProvider({
            name: "Credentials",
            credentials: {
                username: { label: "Username", type: "text", placeholder: "admin" },
                password: { label: "Password", type: "password" },
            },
            async authorize(credentials) {
                if (credentials.username === "admin" && credentials.password === "password") {
                    return { id: "1", name: "Admin User", role: "admin" };
                }
                return null;
            },
        }),
    ],
    callbacks: {
        async session({ session, token }) {
            session.user.role = token.role; // 역할 추가
            return session;
        },
        async jwt({ token, user }) {
            if (user) token.role = user.role; // 역할 토큰에 저장
            return token;
        },
    },
    session: {
        strategy: "jwt",
    },
};

const handler = NextAuth(authOptions);
export { handler as GET, handler as POST };
