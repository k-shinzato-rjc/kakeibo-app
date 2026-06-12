"use client"

import React from "react"
import {useState, useEffect} from "react"
import { useRouter,useSearchParams } from "next/navigation"
import { useForm } from "react-hook-form"

// カテゴリーデータの型
type Category = {
    id : number
    name : string
    type : string
}

// 履歴データの型
type Transaction = {
    id : number
    categoryId: number
    amount: number
    transactionDate: string
    memo: string
    categories : Category
}

// SpringBootパス
const springURL = "http://localhost:8080"

// 編集画面
export default function EditView(){

    // 履歴一覧から転送された取引IDを取得
    const searchParams = useSearchParams()
    const paramId = searchParams.get("paramId")

    // 画面遷移用
    const router = useRouter()

    // ID該当の履歴データ格納用ステートメント
    const[transaction, setTransaction] = useState<Transaction>()
    // カテゴリースト格納用ステートメント
    const[categories, setCategories] = useState<Category[]>()

    // useForm機能
    const{register,handleSubmit,setError, reset, formState : {errors,isSubmitting}} = useForm<Transaction>()

    // 読み込み時、取引ID該当の履歴1件取得＆全カテゴリーリスト取得 → ステートメントへ格納
    useEffect(() => {
        const FetchData = async () => {
            try{
                const responseTransaction = await fetch(springURL + `/api/transactions/search?id=${paramId}`, {method : "post" , credentials : "include"})
                const responseCategories = await fetch(springURL + "/api/categories")
                if(!(responseTransaction.ok && responseCategories.ok)){
                    alert("データの取得に失敗しました")
                    return;
                }

                const jsonT = await responseTransaction.json()
                const jsonC = await responseCategories.json()
                setTransaction(jsonT)
                setCategories(jsonC)
                
                // Formの内容も初期化
                reset(jsonT)

            } catch(error){
                console.log(error)
                alert("通信エラーが発生しました")
                return;
            }
        }
        FetchData();
    },[])

    const Edit = async (data : Transaction) => {
        if(!window.confirm("この内容で履歴編集を行いますか？")){
            return;
        }

        try{
            const response = await fetch(springURL + "/api/transactions/edit", {
                method : "post",
                credentials : "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            })

            console.log(response);

        }catch(error){
            console.log(error)
            alert("通信に失敗しました")
            return;
        }

        alert("履歴を編集しました！")
    }

    return(
        <div className="min-h-screen bg-slate-50/50 py-12 px-4 sm:px-6 lg:px-8 text-slate-700">
            <div className="max-w-md mx-auto">
                
                {/* ヘッダーナビゲーション */}
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 mb-8">
                    <h1 className="text-xl font-bold tracking-tight text-slate-900">履歴編集</h1>{" "}
                    <div className="flex items-center gap-2">
                        <button 
                            onClick={() => router.push("/transactions")}
                            className="px-3 py-1.5 text-xs font-medium text-slate-600 bg-white border border-slate-200 rounded-md shadow-sm hover:bg-slate-50 transition-colors"
                        >
                            履歴一覧へ戻る
                        </button>
                        <button 
                            onClick={() => router.push("/")}
                            className="px-3 py-1.5 text-xs font-medium text-slate-600 bg-white border border-slate-200 rounded-md shadow-sm hover:bg-slate-50 transition-colors"
                        >
                            ダッシュボードへ戻る
                        </button>
                    </div>
                </div>

                {/* フォーム*/}
                <div className="bg-white rounded-xl border border-slate-100 shadow-sm p-6 sm:p-8">
                    <form onSubmit={handleSubmit(Edit)} className="space-y-5">
                        
                        {/* カテゴリー */}
                        <div className="flex flex-col gap-1.5">
                            <label htmlFor="categoryId" className="text-xs font-semibold text-slate-500 uppercase tracking-wider">カテゴリー名</label>
                            <select 
                                id="categoryId"
                                {...register("categoryId" , {valueAsNumber: true})}
                                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all cursor-pointer"
                            >
                                <option value={transaction?.categoryId}>変更無し</option>
                                {categories?.map((c,index) => (
                                    <option key={index} value={c.id}>{c.name}</option>
                                ))}
                            </select>
                        </div>

                        {/* 金額 */}
                        <div className="flex flex-col gap-1.5">
                            <label htmlFor="amount" className="text-xs font-semibold text-slate-500 uppercase tracking-wider">金額</label>
                            <div className="relative">
                                <span className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 text-sm">¥</span>
                                <input 
                                    type="number" 
                                    id="amount"
                                    {...register("amount" , {valueAsNumber : true})}
                                    className="w-full pl-7 pr-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm font-medium text-slate-900 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all"
                                />
                            </div>
                        </div>

                        {/* 取引日 */}
                        <div className="flex flex-col gap-1.5">
                            <label htmlFor="transactionDate" className="text-xs font-semibold text-slate-500 uppercase tracking-wider">取引日</label>
                            <input 
                                type="date" 
                                id="transactionDate"
                                {...register("transactionDate")}
                                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all"
                            />
                        </div>

                        {/* 詳細 */}
                        <div className="flex flex-col gap-1.5">
                            <label htmlFor="memo" className="text-xs font-semibold text-slate-500 uppercase tracking-wider">詳細</label>
                            <textarea 
                                id="memo"
                                {...register("memo")}
                                rows={3}
                                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all resize-none"
                            ></textarea>
                        </div>

                        {/* 隠しフィールド群 */}
                        <input type="hidden" {...register("id", {valueAsNumber : true})}></input>
                        <input type="hidden" {...register("categories.id", {valueAsNumber : true})}></input>
                        <input type="hidden" {...register("categories.name")}></input>
                        <input type="hidden" {...register("categories.type")}></input>
                        
                        {/* 送信ボタン */}
                        <button 
                            type="submit" 
                            disabled={isSubmitting}
                            className="w-full mt-2 px-4 py-2.5 text-sm font-medium text-white bg-blue-600 rounded-lg shadow-sm hover:bg-blue-700 active:bg-blue-800 disabled:bg-blue-400 disabled:cursor-not-allowed transition-colors duration-150"
                        >
                            {isSubmitting ? "送信中..." : "履歴を編集する"}
                        </button>
                    </form>
                </div>

            </div>
        </div>
    )
    
}