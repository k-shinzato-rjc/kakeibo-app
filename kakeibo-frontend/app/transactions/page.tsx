"use client";

import React from "react";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { number } from "zod";

// 取引履歴データ 型
type Transaction = {
    id : number
    categoryId: number
    amount: number
    transactionDate: Date
    memo: string
    categories : { 
        id: number
        name: string
        type: string
    }
}

  // SpringBootリクエスト
const springURL = "http://localhost:8080";

// 現在年月を取得
const now = new Date();
const currentYear = now.getFullYear();
const currentMonth = now.getMonth() + 1;

export default function HistoryView(){

    // 画面遷移用
    const router = useRouter();

    // 取引履歴リスト 格納用
    const[ transactions, setTransactions] = useState<Transaction[]>();

    // 選択した年と月を管理するステートメント（初期値は現在の年月）
    const[ year, setYear ] = useState<number | string>(currentYear);
    const[ month, setMonth ] = useState<number | string>(currentMonth);
    
    // 全ての取引履歴を取得し、ステートメントに格納
    const FetchData = async () => {
        try{
            const response = await fetch(springURL + "/api/transactions/all", {credentials : "include"});

            if(!response.ok){
                alert("データを取得できませんでした");
                return;
            }

            const json = await response.json();
            setTransactions(json);

        }catch(error){
            alert("通信エラーが発生しました");
            return;
        }
    }

    // 画面読み込み時、全ての取引履歴を取得し、ステートメントに格納
    useEffect(() => {

        FetchData();

    },[]);

    // 選択された年月から絞り込みされた履歴リストを取得、ステートメントへ格納
    const SelectPeriod = async () => {
        try{
            const response = await fetch(springURL + `/api/transactions/select?year=${year}&month=${month}`, {method : "post", credentials : "include"});

            if(!response.ok){
                alert("データを取得できませんでした。");
                return;
            }

            const json = await response.json();
            setTransactions(json);

        }catch(error){
            alert("通信エラーが発生しました");
            return;
        }
    }

    // ID該当の履歴を削除し、削除後の全履歴リストを返す → ステートメントに格納
    const DeleteById = async(id : number) => {
        try{
            const response = await fetch(springURL + `/api/transactions/delete?id=${id}`,{method : "post", credentials : "include"});
            
            if(!response.ok){
                alert("データを取得できませんでした");
                return;
            }

            const json = await response.json();
            setTransactions(json);

        }catch(error){
            alert("通信エラーが発生しました");
            return;
        }
    }

    return(
        <div className="min-h-screen bg-slate-50 py-12 px-4 sm:px-6 lg:px-8 text-slate-800">
            <div className="max-w-6xl mx-auto space-y-6">
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
                    <div>
                        <div className="text-2xl font-bold text-slate-900 tracking-tight">収支履歴一覧</div>
                    </div>
                    <button 
                        onClick={() => router.push("/")}
                        className="inline-flex items-center justify-center px-4 py-2.5 text-sm font-medium text-slate-700 bg-white border border-slate-200 rounded-xl hover:bg-slate-50 hover:text-slate-900 transition-all duration-200 shadow-sm"
                    >
                        ダッシュボードへ戻る
                    </button>
                </div>

                <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6 items-end">
                        <div className="space-y-2">
                            {/* 1年前からリアルタイムの年、1年後まで選択可 */}
                            <span className="block text-xs font-semibold text-slate-500 uppercase tracking-wider">取引年</span>
                            <div className="relative">
                                <select
                                    onChange={(e) => setYear(e.target.value === "" ? "" : Number(e.target.value))}
                                    className="w-full pl-4 pr-10 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all appearance-none text-slate-700 font-medium"
                                >
                                    <option value="">未選択</option>
                                    <option value={currentYear - 1}>{currentYear - 1}</option>
                                    <option value={currentYear}>{currentYear}</option>
                                    <option value={currentYear + 1}>{currentYear + 1}</option>
                                </select>
                                <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-4 text-slate-400">
                                    <svg className="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
                                        <path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z"/>
                                    </svg>
                                </div>
                            </div>
                        </div>

                        <div className="space-y-2">
                            {/* 1～12月を選択 */}
                            <span className="block text-xs font-semibold text-slate-500 uppercase tracking-wider">取引月</span>
                            <div className="relative">
                                <select
                                    onChange={(e) => setMonth(e.target.value === "" ? "" : Number(e.target.value))}
                                    className="w-full pl-4 pr-10 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all appearance-none text-slate-700 font-medium"
                                >
                                    <option value="">未選択</option>
                                    {[...Array(12)].map((_,index) => (
                                        <option key={index} value={index + 1}>{index + 1}</option>
                                    ))}
                                </select>
                                <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-4 text-slate-400">
                                    <svg className="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
                                        <path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z"/>
                                    </svg>
                                </div>
                            </div>
                        </div>

                        <div className="flex items-center gap-3">
                            {/* ボタン押下 → 選択年月から履歴を取得する関数 実行 */}
                            <button 
                                onClick={() => SelectPeriod()}
                                className="flex-1 bg-blue-600 text-white h-[42px] px-6 rounded-xl font-semibold hover:bg-blue-700 active:scale-[0.98] transition-all shadow-sm shadow-blue-500/10 text-sm"
                            >
                                検索
                            </button>
                            <button 
                                onClick={() => FetchData()}
                                className="flex-1 bg-slate-100 text-slate-700 h-[42px] px-6 rounded-xl font-semibold hover:bg-slate-200 active:scale-[0.98] transition-all text-sm"
                            >
                                指定解除
                            </button>
                        </div>
                    </div>
                </div>

                <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
                    <div className="overflow-x-auto">
                        {/**履歴リストを表示 */}
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="bg-slate-50/70 border-b border-slate-100 text-slate-400 text-[11px] font-bold uppercase tracking-wider">
                                    <th className="px-6 py-4">履歴番号</th>
                                    <th className="px-6 py-4">カテゴリー</th>
                                    <th className="px-6 py-4">金額</th>
                                    <th className="px-6 py-4">取引日</th>
                                    <th className="px-6 py-4">詳細</th>
                                    <th className="px-6 py-4 text-right">オプション</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-slate-100 bg-white">
                                {transactions?.map((t, index) => (
                                    <tr key={index} className="hover:bg-slate-50/50 transition-colors duration-150 group">
                                        <td className="px-6 py-4.5 text-sm text-slate-500 font-medium">#{t.id}</td>
                                        <td className="px-6 py-4.5">
                                            <span className="inline-flex items-center px-2.5 py-1 rounded-lg text-xs font-semibold bg-slate-100 text-slate-700 group-hover:bg-white transition-colors border border-transparent group-hover:border-slate-200">
                                                {t.categories.name}
                                            </span>
                                        </td>
                                        <td className={`px-6 py-4.5 text-base font-bold ${
                                            t.categories.type === "INCOME" ? "text-lime-600" : 
                                            t.categories.type === "EXPENSE" ? "text-red-600" : "text-slate-900"
                                        }`}>
                                            ¥{t.amount.toLocaleString()}
                                        </td>
                                        <td className="px-6 py-4.5 text-sm text-slate-500">
                                            {new Date(t.transactionDate).toLocaleDateString('ja-JP')}
                                        </td>
                                        <td className="px-6 py-4.5 text-sm text-slate-600 max-w-xs truncate">
                                            {t.memo}
                                        </td>
                                        <td className="px-6 py-4.5 text-right">
                                            <div className="inline-flex gap-1">
                                                <button className="px-3 py-1.5 text-xs font-semibold text-slate-600 bg-slate-50 border border-slate-200 rounded-lg hover:bg-slate-100 hover:text-slate-800 transition-all"
                                                    onClick={() => router.push(`transactions/edit?paramId=${t.id}`)}>
                                                    編集
                                                </button>
                                                <button 
                                                    onClick={() => DeleteById(t.id)}
                                                    className="px-3 py-1.5 text-xs font-semibold text-red-600 bg-red-50/50 border border-red-100 rounded-lg hover:bg-red-50 hover:text-red-700 transition-all"
                                                >
                                                    削除
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>

                    {(!transactions || transactions.length === 0) && (
                        <div className="text-center py-20 bg-white">
                            <div className="text-sm font-medium text-slate-400">データが見つかりません。</div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}