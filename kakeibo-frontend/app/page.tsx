"use client";

import React from "react";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation"
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from "recharts";

type SummaryData = {
  year : number
  month : number
  totalIncome : number
  totalExpense : number
  balance : number
  expenseByCategory : {
    categoryName : string
    amount : number
  }[]
}

const COLORS = ["#3b82f6", "#10b981", "#f59e0b", "#ef4444"];

// ダッシュボード画面の表示
export default function DashBoardView(){

  const[summary, setSummary] = useState<SummaryData>();
  const router = useRouter();
  const springURL = "http://localhost:8080";
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
    const fetchData = async () => {

      // 現在の年・月を取得
      const today = new Date();
      const year = today.getFullYear();
      const month = today.getMonth() + 1;

      try{
        // サマリーデータ取得 → ステートメント格納
        const response = await fetch(springURL + `/api/summary?year=${year}&month=${month}`, {credentials : "include"});
        const json = await response.json();
        setSummary(json);

        if(!response.ok){
          console.log("通信失敗");
        }

      }catch(error){
        console.log(error);
      }
    }

    fetchData();

  },[]);

  return (
    <div style={{ maxWidth: "900px", margin: "40px auto", padding: "40px", borderRadius: "32px", backgroundColor: "#ffffff", boxShadow: "0 25px 50px -12px rgba(0, 0, 0, 0.1)", fontFamily: "'Inter', sans-serif", color: "#1e293b" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "40px" }}>
        <div>
          <div style={{ fontSize: "32px", fontWeight: "800", marginBottom: "8px", color: "#0f172a" }}>新里家 家計簿</div>
          <div style={{ fontSize: "16px", fontWeight: "500", color: "#64748b" }}>{summary?.year}年 {summary?.month}月 サマリー</div>
        </div>
        <div style={{ display: "flex", gap: "16px" }}>
          <button onClick={() => router.push("/transactions/new")} style={{ padding: "14px 28px", borderRadius: "16px", backgroundColor: "#0f172a", color: "#ffffff", border: "none", fontWeight: "700", cursor: "pointer", transition: "opacity 0.2s" }}>収支入力</button>
          <button onClick={() => router.push("/transactions")} style={{ padding: "14px 28px", borderRadius: "16px", backgroundColor: "#f1f5f9", color: "#475569", border: "none", fontWeight: "700", cursor: "pointer", transition: "background 0.2s" }}>履歴一覧</button>
        </div>
      </div>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "40px", alignItems: "center" }}>
        <div style={{ display: "flex", flexDirection: "column", gap: "20px" }}>
          <div style={{ padding: "24px", backgroundColor: "#eff6ff", borderRadius: "24px", display: "flex", justifyContent: "space-between", alignItems: "center", border: "1px solid #dbeafe" }}><span style={{ color: "#1e40af", fontWeight: "600", fontSize: "16px" }}>収入合計</span><span style={{ fontSize: "24px", fontWeight: "800", color: "#2563eb" }}>{summary?.totalIncome?.toLocaleString()} 円</span></div>
          <div style={{ padding: "24px", backgroundColor: "#fef2f2", borderRadius: "24px", display: "flex", justifyContent: "space-between", alignItems: "center", border: "1px solid #fee2e2" }}><span style={{ color: "#991b1b", fontWeight: "600", fontSize: "16px" }}>出費合計</span><span style={{ fontSize: "24px", fontWeight: "800", color: "#dc2626" }}>{summary?.totalExpense?.toLocaleString()} 円</span></div>
          <div style={{ padding: "24px", backgroundColor: summary?.balance !== undefined && summary.balance < 0 ? "#fef2f2" : "#f0fdf4", borderRadius: "24px", display: "flex", justifyContent: "space-between", alignItems: "center", border: summary?.balance !== undefined && summary.balance < 0 ? "1px solid #fee2e2" : "1px solid #dcfce7" }}><span style={{ color: summary?.balance !== undefined && summary.balance < 0 ? "#991b1b" : "#166534", fontWeight: "600", fontSize: "16px" }}>当月残高</span><span style={{ fontSize: "32px", fontWeight: "800", color: summary?.balance !== undefined && summary.balance < 0 ? "#dc2626" : "#059669" }}>{summary?.balance !== undefined && summary.balance < 0 ? (summary.balance * -1).toLocaleString() : summary?.balance?.toLocaleString()} 円</span></div>
        </div>

        {isMounted && (

          // グラフの親要素に最小幅と高さを明示
          <div style={{ backgroundColor: "#f8fafc", borderRadius: "24px", padding: "20px", border: "1px solid #e2e8f0", minHeight: "350px", width: "100%" }}>
          <ResponsiveContainer width="100%" height={350}>

            {/* グラフレイアウト設定 */}
            <PieChart>

              {/* グラフ本体 */}
              <Pie data={summary?.expenseByCategory} dataKey="amount" nameKey="categoryName" cx="50%" cy="50%" innerRadius={70} outerRadius={100} paddingAngle={5}>
               {summary?.expenseByCategory?.map((expense , index) => (

                  // グラフ要素
                 <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} style={{ outline: "none" }}/>
               ))}
              </Pie>
              <Tooltip contentStyle={{ backgroundColor: "#1e293b", borderRadius: "16px", border: "none", color: "#ffffff", padding: "12px 18px", fontSize: "15px", boxShadow: "0 10px 15px -3px rgba(0, 0, 0, 0.1)" }} itemStyle={{ color: "#ffffff" }} formatter={(value) => `${Number(value).toLocaleString()}円`}></Tooltip>
              <Legend verticalAlign="bottom" iconType="circle" iconSize={12} wrapperStyle={{ fontSize: "14px", fontWeight: "600", color: "#475569", paddingTop: "20px" }}/>
            </PieChart>
          </ResponsiveContainer>
          </div>
         )}
      </div>
    </div>
  );

}