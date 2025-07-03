"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"
import {
  User,
  LogOut,
  BookOpen,
  Award,
  Clock,
  Plus,
  Edit,
  Trash2,
  Play,
  CheckCircle,
  Settings,
  TrendingUp,
  Users,
  BarChart3,
  Building2,
  MessageCircle,
  Lock,
  Unlock,
  GraduationCap,
} from "lucide-react"
import AdminPanel from "./admin-panel"
import EvaluationTaker from "./evaluation-taker"
import CentersManagement from "./centers-management"
import StudentsManagement from "./students-management"
import Chatbot from "./chatbot"

interface DashboardProps {
  userRole: "ADMIN" | "NORMAL"
  currentUser: any
  onLogout: () => void
}

interface Category {
  id: number
  name: string
  description: string
  order: number
  color: string
  icon: string
  prerequisiteId?: number
}

interface Evaluation {
  id: number
  title: string
  description: string
  categoryId: number
  category: string
  duration: number
  questions: number
  difficulty: "Básico" | "Intermedio" | "Avanzado"
  status: "Activa" | "Inactiva"
  completions: number
  createdAt: string
  updatedAt: string
  prerequisiteEvaluationId?: number
  passingScore: number
}

interface UserResult {
  id: number
  evaluationId: number
  evaluationTitle: string
  categoryId: number
  score: number
  maxScore: number
  percentage: number
  completedAt: string
  certified: boolean
  timeSpent: number
}

export default function Dashboard({ userRole, currentUser, onLogout }: DashboardProps) {
  const [activeTab, setActiveTab] = useState("overview")
  const [selectedEvaluation, setSelectedEvaluation] = useState<Evaluation | null>(null)
  const [isEditingEvaluation, setIsEditingEvaluation] = useState(false)
  const [evaluations, setEvaluations] = useState<Evaluation[]>([])
  const [categories, setCategories] = useState<Category[]>([])
  const [userResults, setUserResults] = useState<UserResult[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [showChatbot, setShowChatbot] = useState(false)

  // Inicializar datos
  useEffect(() => {
    const initializeData = () => {
      const defaultCategories: Category[] = [
        {
          id: 1,
          name: "Fundamentos de Programación",
          description: "Conceptos básicos de programación y lógica",
          order: 1,
          color: "bg-blue-100 text-blue-800 border-blue-200",
          icon: "💻",
        },
        {
          id: 2,
          name: "Desarrollo Web Frontend",
          description: "HTML, CSS, JavaScript y frameworks modernos",
          order: 2,
          color: "bg-green-100 text-green-800 border-green-200",
          icon: "🌐",
          prerequisiteId: 1,
        },
        {
          id: 3,
          name: "Desarrollo Web Backend",
          description: "Servidores, APIs y bases de datos",
          order: 3,
          color: "bg-purple-100 text-purple-800 border-purple-200",
          icon: "⚙️",
          prerequisiteId: 2,
        },
        {
          id: 4,
          name: "Desarrollo Full Stack",
          description: "Integración completa de tecnologías web",
          order: 4,
          color: "bg-orange-100 text-orange-800 border-orange-200",
          icon: "🚀",
          prerequisiteId: 3,
        },
        {
          id: 5,
          name: "Diseño UX/UI",
          description: "Experiencia de usuario y diseño de interfaces",
          order: 5,
          color: "bg-pink-100 text-pink-800 border-pink-200",
          icon: "🎨",
        },
      ]

      const defaultEvaluations: Evaluation[] = [
        {
          id: 1,
          title: "Fundamentos de JavaScript",
          description: "Evaluación completa de conceptos fundamentales de JavaScript",
          categoryId: 1,
          category: "Fundamentos de Programación",
          duration: 45,
          questions: 20,
          difficulty: "Básico",
          status: "Activa",
          completions: 156,
          createdAt: "2024-01-01",
          updatedAt: "2024-01-15",
          passingScore: 70,
        },
        {
          id: 2,
          title: "HTML y CSS Avanzado",
          description: "Maquetación moderna y responsive design",
          categoryId: 2,
          category: "Desarrollo Web Frontend",
          duration: 50,
          questions: 18,
          difficulty: "Intermedio",
          status: "Activa",
          completions: 89,
          createdAt: "2024-01-05",
          updatedAt: "2024-01-20",
          prerequisiteEvaluationId: 1,
          passingScore: 75,
        },
        {
          id: 3,
          title: "React Fundamentals",
          description: "Componentes, hooks y estado en React",
          categoryId: 2,
          category: "Desarrollo Web Frontend",
          duration: 60,
          questions: 25,
          difficulty: "Intermedio",
          status: "Activa",
          completions: 67,
          createdAt: "2024-01-10",
          updatedAt: "2024-01-25",
          prerequisiteEvaluationId: 2,
          passingScore: 75,
        },
        {
          id: 4,
          title: "Node.js y Express",
          description: "Desarrollo de APIs REST con Node.js",
          categoryId: 3,
          category: "Desarrollo Web Backend",
          duration: 55,
          questions: 22,
          difficulty: "Avanzado",
          status: "Activa",
          completions: 45,
          createdAt: "2024-01-12",
          updatedAt: "2024-01-28",
          prerequisiteEvaluationId: 3,
          passingScore: 80,
        },
        {
          id: 5,
          title: "Diseño de Experiencia de Usuario",
          description: "Principios de UX y metodologías de diseño",
          categoryId: 5,
          category: "Diseño UX/UI",
          duration: 40,
          questions: 15,
          difficulty: "Intermedio",
          status: "Activa",
          completions: 134,
          createdAt: "2024-01-08",
          updatedAt: "2024-01-22",
          passingScore: 70,
        },
      ]

      const defaultResults: UserResult[] = [
        {
          id: 1,
          evaluationId: 1,
          evaluationTitle: "Fundamentos de JavaScript",
          categoryId: 1,
          score: 85,
          maxScore: 100,
          percentage: 85,
          completedAt: "2024-01-15T10:30:00Z",
          certified: true,
          timeSpent: 42,
        },
      ]

      setCategories(defaultCategories)
      setEvaluations(defaultEvaluations)
      if (userRole === "NORMAL") {
        setUserResults(defaultResults)
      }
      setIsLoading(false)
    }

    initializeData()
  }, [userRole])

  const isEvaluationUnlocked = (evaluation: Evaluation): boolean => {
    if (!evaluation.prerequisiteEvaluationId) return true

    return userResults.some((result) => result.evaluationId === evaluation.prerequisiteEvaluationId && result.certified)
  }

  const getCategoryProgress = (categoryId: number): number => {
    const categoryEvaluations = evaluations.filter((e) => e.categoryId === categoryId)
    const completedEvaluations = userResults.filter(
      (r) => categoryEvaluations.some((e) => e.id === r.evaluationId) && r.certified,
    )

    if (categoryEvaluations.length === 0) return 0
    return Math.round((completedEvaluations.length / categoryEvaluations.length) * 100)
  }

  const handleStartEvaluation = (evaluation: Evaluation) => {
    if (!isEvaluationUnlocked(evaluation)) {
      alert("Debes completar la evaluación prerequisito primero")
      return
    }
    setSelectedEvaluation(evaluation)
    setActiveTab("take-evaluation")
  }

  const handleEditEvaluation = (evaluation: Evaluation) => {
    setSelectedEvaluation(evaluation)
    setIsEditingEvaluation(true)
    setActiveTab("admin")
  }

  const handleEvaluationComplete = (result: any) => {
    const newResult: UserResult = {
      id: Date.now(),
      evaluationId: selectedEvaluation!.id,
      evaluationTitle: selectedEvaluation!.title,
      categoryId: selectedEvaluation!.categoryId,
      score: result.score,
      maxScore: result.maxScore,
      percentage: result.percentage,
      completedAt: new Date().toISOString(),
      certified: result.certified,
      timeSpent: result.timeSpent || 0,
    }

    setUserResults((prev) => [newResult, ...prev])
    setSelectedEvaluation(null)
    setActiveTab("overview")
  }

  const handleDeleteEvaluation = (evaluationId: number) => {
    setEvaluations((prev) => prev.filter((e) => e.id !== evaluationId))
  }

  const handleUpdateEvaluation = (updatedEvaluation: Evaluation) => {
    setEvaluations((prev) => prev.map((e) => (e.id === updatedEvaluation.id ? updatedEvaluation : e)))
  }

  const handleCreateEvaluation = (
    newEvaluation: Omit<Evaluation, "id" | "completions" | "createdAt" | "updatedAt">,
  ) => {
    const evaluation: Evaluation = {
      ...newEvaluation,
      id: Date.now(),
      completions: 0,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    }
    setEvaluations((prev) => [evaluation, ...prev])
  }

  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty) {
      case "Básico":
        return "bg-green-100 text-green-800 border-green-200"
      case "Intermedio":
        return "bg-yellow-100 text-yellow-800 border-yellow-200"
      case "Avanzado":
        return "bg-red-100 text-red-800 border-red-200"
      default:
        return "bg-gray-100 text-gray-800 border-gray-200"
    }
  }

  const getStatusColor = (status: string) => {
    return status === "Activa"
      ? "bg-green-100 text-green-800 border-green-200"
      : "bg-gray-100 text-gray-800 border-gray-200"
  }

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("es-PE", {
      year: "numeric",
      month: "long",
      day: "numeric",
    })
  }

  const activeEvaluations = evaluations.filter((e) => e.status === "Activa")
  const userCertificates = userResults.filter((r) => r.certified)
  const averageScore =
    userResults.length > 0 ? Math.round(userResults.reduce((acc, r) => acc + r.percentage, 0) / userResults.length) : 0

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Cargando dashboard...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b sticky top-0 z-40">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <div className="bg-blue-600 p-2 rounded-lg mr-3">
                <Award className="h-6 w-6 text-white" />
              </div>
              <div>
                <h1 className="text-xl font-semibold text-gray-900">SkillCertify</h1>
                <p className="text-xs text-gray-500">Panel de Control</p>
              </div>
            </div>
            <div className="flex items-center space-x-4">
              <Button variant="ghost" size="sm" onClick={() => setShowChatbot(!showChatbot)} className="relative">
                <MessageCircle className="h-5 w-5" />
                <span className="sr-only">Asistente Virtual</span>
              </Button>
              <div className="flex items-center space-x-3">
                <Avatar className="h-8 w-8">
                  <AvatarFallback className="bg-blue-100 text-blue-600 text-sm">
                    {currentUser?.name
                      ?.split(" ")
                      .map((n: string) => n[0])
                      .join("")
                      .toUpperCase() || "U"}
                  </AvatarFallback>
                </Avatar>
                <div className="hidden sm:block">
                  <p className="text-sm font-medium text-gray-900">{currentUser?.name}</p>
                  <p className="text-xs text-gray-500">{currentUser?.email}</p>
                </div>
              </div>
              <Badge variant={userRole === "ADMIN" ? "default" : "secondary"} className="hidden sm:flex">
                {userRole === "ADMIN" ? (
                  <>
                    <Settings className="h-3 w-3 mr-1" />
                    Administrador
                  </>
                ) : (
                  <>
                    <User className="h-3 w-3 mr-1" />
                    Estudiante
                  </>
                )}
              </Badge>
              <Button variant="outline" size="sm" onClick={onLogout}>
                <LogOut className="h-4 w-4 mr-2" />
                <span className="hidden sm:inline">Cerrar Sesión</span>
              </Button>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <Tabs value={activeTab} onValueChange={setActiveTab}>
          <TabsList className={`grid w-full ${userRole === "ADMIN" ? "grid-cols-6" : "grid-cols-3"} mb-8`}>
            <TabsTrigger value="overview" className="flex items-center gap-2">
              <BarChart3 className="h-4 w-4" />
              <span className="hidden sm:inline">Resumen</span>
            </TabsTrigger>
            <TabsTrigger value="evaluations" className="flex items-center gap-2">
              <BookOpen className="h-4 w-4" />
              <span className="hidden sm:inline">Evaluaciones</span>
            </TabsTrigger>
            <TabsTrigger value="learning-path" className="flex items-center gap-2">
              <GraduationCap className="h-4 w-4" />
              <span className="hidden sm:inline">Ruta de Aprendizaje</span>
            </TabsTrigger>
            {userRole === "ADMIN" && (
              <>
                <TabsTrigger value="admin" className="flex items-center gap-2">
                  <Settings className="h-4 w-4" />
                  <span className="hidden sm:inline">Administración</span>
                </TabsTrigger>
                <TabsTrigger value="centers" className="flex items-center gap-2">
                  <Building2 className="h-4 w-4" />
                  <span className="hidden sm:inline">Centros</span>
                </TabsTrigger>
                <TabsTrigger value="students" className="flex items-center gap-2">
                  <Users className="h-4 w-4" />
                  <span className="hidden sm:inline">Estudiantes</span>
                </TabsTrigger>
              </>
            )}
            {selectedEvaluation && (
              <TabsTrigger value="take-evaluation" className="flex items-center gap-2">
                <Play className="h-4 w-4" />
                <span className="hidden sm:inline">Evaluación</span>
              </TabsTrigger>
            )}
          </TabsList>

          {/* Overview Tab */}
          <TabsContent value="overview" className="space-y-8">
            {/* Stats Cards */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
              <Card className="hover:shadow-md transition-shadow">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium text-gray-600">Evaluaciones Disponibles</CardTitle>
                  <BookOpen className="h-4 w-4 text-blue-600" />
                </CardHeader>
                <CardContent>
                  <div className="text-2xl font-bold text-gray-900">{activeEvaluations.length}</div>
                  <p className="text-xs text-gray-500 mt-1">{evaluations.length} evaluaciones totales</p>
                </CardContent>
              </Card>

              <Card className="hover:shadow-md transition-shadow">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium text-gray-600">Certificados Obtenidos</CardTitle>
                  <Award className="h-4 w-4 text-green-600" />
                </CardHeader>
                <CardContent>
                  <div className="text-2xl font-bold text-gray-900">{userCertificates.length}</div>
                  <p className="text-xs text-gray-500 mt-1">{userResults.length} evaluaciones completadas</p>
                </CardContent>
              </Card>

              <Card className="hover:shadow-md transition-shadow">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium text-gray-600">Promedio de Puntuación</CardTitle>
                  <TrendingUp className="h-4 w-4 text-purple-600" />
                </CardHeader>
                <CardContent>
                  <div className="text-2xl font-bold text-gray-900">{averageScore}%</div>
                  <p className="text-xs text-gray-500 mt-1">Basado en {userResults.length} evaluaciones</p>
                </CardContent>
              </Card>

              <Card className="hover:shadow-md transition-shadow">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium text-gray-600">
                    {userRole === "ADMIN" ? "Total Participantes" : "Categorías Completadas"}
                  </CardTitle>
                  <Users className="h-4 w-4 text-orange-600" />
                </CardHeader>
                <CardContent>
                  <div className="text-2xl font-bold text-gray-900">
                    {userRole === "ADMIN"
                      ? evaluations.reduce((acc, e) => acc + e.completions, 0)
                      : categories.filter((c) => getCategoryProgress(c.id) === 100).length}
                  </div>
                  <p className="text-xs text-gray-500 mt-1">
                    {userRole === "ADMIN" ? "Evaluaciones completadas" : `de ${categories.length} categorías`}
                  </p>
                </CardContent>
              </Card>
            </div>

            {/* Learning Progress for Normal Users */}
            {userRole === "NORMAL" && (
              <Card>
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <GraduationCap className="h-5 w-5 text-blue-600" />
                    Mi Progreso de Aprendizaje
                  </CardTitle>
                  <CardDescription>Avance por categorías de conocimiento</CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {categories.map((category) => {
                      const progress = getCategoryProgress(category.id)
                      const categoryEvaluations = evaluations.filter((e) => e.categoryId === category.id)
                      const isUnlocked =
                        !category.prerequisiteId ||
                        (categories.find((c) => c.id === category.prerequisiteId) &&
                          getCategoryProgress(category.prerequisiteId) === 100)

                      return (
                        <div key={category.id} className="p-4 border rounded-lg">
                          <div className="flex items-center justify-between mb-2">
                            <div className="flex items-center gap-3">
                              <span className="text-2xl">{category.icon}</span>
                              <div>
                                <h4 className="font-medium text-gray-900 flex items-center gap-2">
                                  {category.name}
                                  {isUnlocked ? (
                                    <Unlock className="h-4 w-4 text-green-600" />
                                  ) : (
                                    <Lock className="h-4 w-4 text-gray-400" />
                                  )}
                                </h4>
                                <p className="text-sm text-gray-500">{category.description}</p>
                              </div>
                            </div>
                            <div className="text-right">
                              <div className="text-lg font-bold text-gray-900">{progress}%</div>
                              <div className="text-sm text-gray-500">{categoryEvaluations.length} evaluaciones</div>
                            </div>
                          </div>
                          <div className="w-full bg-gray-200 rounded-full h-2">
                            <div
                              className="bg-blue-600 h-2 rounded-full transition-all duration-300"
                              style={{ width: `${progress}%` }}
                            ></div>
                          </div>
                        </div>
                      )
                    })}
                  </div>
                </CardContent>
              </Card>
            )}

            {/* Recent Results for Normal Users */}
            {userRole === "NORMAL" && userResults.length > 0 && (
              <Card>
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <CheckCircle className="h-5 w-5 text-green-600" />
                    Mis Resultados Recientes
                  </CardTitle>
                  <CardDescription>Historial de evaluaciones completadas</CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {userResults.slice(0, 5).map((result) => (
                      <div
                        key={result.id}
                        className="flex items-center justify-between p-4 border rounded-lg hover:bg-gray-50 transition-colors"
                      >
                        <div className="flex-1">
                          <h4 className="font-medium text-gray-900">{result.evaluationTitle}</h4>
                          <p className="text-sm text-gray-500">
                            Completado el {formatDate(result.completedAt)} • {result.timeSpent} minutos
                          </p>
                        </div>
                        <div className="flex items-center space-x-4">
                          <div className="text-right">
                            <p className="font-medium text-gray-900">
                              {result.score}/{result.maxScore}
                            </p>
                            <p className="text-sm text-gray-500">{result.percentage}%</p>
                          </div>
                          {result.certified ? (
                            <Badge className="bg-green-100 text-green-800 border-green-200">
                              <Award className="h-3 w-3 mr-1" />
                              Certificado
                            </Badge>
                          ) : (
                            <Badge variant="outline" className="text-gray-600">
                              No Certificado
                            </Badge>
                          )}
                        </div>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            )}

            {/* Popular Evaluations for Admin */}
            {userRole === "ADMIN" && (
              <Card>
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <TrendingUp className="h-5 w-5 text-blue-600" />
                    Evaluaciones Más Populares
                  </CardTitle>
                  <CardDescription>Ranking por número de completadas</CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {evaluations
                      .sort((a, b) => b.completions - a.completions)
                      .slice(0, 5)
                      .map((evaluation, index) => (
                        <div
                          key={evaluation.id}
                          className="flex items-center justify-between p-4 border rounded-lg hover:bg-gray-50 transition-colors"
                        >
                          <div className="flex items-center space-x-4">
                            <div className="flex items-center justify-center w-8 h-8 bg-blue-100 text-blue-600 rounded-full font-bold text-sm">
                              #{index + 1}
                            </div>
                            <div>
                              <h4 className="font-medium text-gray-900">{evaluation.title}</h4>
                              <div className="flex items-center gap-2 mt-1">
                                <Badge className="bg-blue-100 text-blue-800 border-blue-200" variant="outline">
                                  {evaluation.category}
                                </Badge>
                                <Badge className={getDifficultyColor(evaluation.difficulty)} variant="outline">
                                  {evaluation.difficulty}
                                </Badge>
                              </div>
                            </div>
                          </div>
                          <div className="text-right">
                            <p className="font-medium text-gray-900">{evaluation.completions}</p>
                            <p className="text-sm text-gray-500">completadas</p>
                          </div>
                        </div>
                      ))}
                  </div>
                </CardContent>
              </Card>
            )}
          </TabsContent>

          {/* Learning Path Tab */}
          <TabsContent value="learning-path" className="space-y-6">
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
              <div>
                <h2 className="text-2xl font-bold text-gray-900">Ruta de Aprendizaje</h2>
                <p className="text-gray-600 mt-1">Progresa paso a paso en tu formación profesional</p>
              </div>
            </div>

            <div className="space-y-6">
              {categories.map((category, index) => {
                const progress = getCategoryProgress(category.id)
                const categoryEvaluations = evaluations.filter(
                  (e) => e.categoryId === category.id && e.status === "Activa",
                )
                const isUnlocked = !category.prerequisiteId || getCategoryProgress(category.prerequisiteId) === 100

                return (
                  <Card
                    key={category.id}
                    className={`${isUnlocked ? "border-blue-200" : "border-gray-200 opacity-75"}`}
                  >
                    <CardHeader>
                      <div className="flex items-center justify-between">
                        <div className="flex items-center gap-4">
                          <div className="flex items-center justify-center w-12 h-12 bg-blue-100 rounded-full">
                            <span className="text-2xl">{category.icon}</span>
                          </div>
                          <div>
                            <CardTitle className="flex items-center gap-2">
                              {category.name}
                              {isUnlocked ? (
                                <Unlock className="h-4 w-4 text-green-600" />
                              ) : (
                                <Lock className="h-4 w-4 text-gray-400" />
                              )}
                            </CardTitle>
                            <CardDescription>{category.description}</CardDescription>
                          </div>
                        </div>
                        <div className="text-right">
                          <div className="text-2xl font-bold text-gray-900">{progress}%</div>
                          <div className="text-sm text-gray-500">Completado</div>
                        </div>
                      </div>
                      <div className="w-full bg-gray-200 rounded-full h-3 mt-4">
                        <div
                          className="bg-blue-600 h-3 rounded-full transition-all duration-300"
                          style={{ width: `${progress}%` }}
                        ></div>
                      </div>
                    </CardHeader>
                    <CardContent>
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        {categoryEvaluations.map((evaluation) => {
                          const isEvalUnlocked = isEvaluationUnlocked(evaluation)
                          const isCompleted = userResults.some((r) => r.evaluationId === evaluation.id && r.certified)

                          return (
                            <div
                              key={evaluation.id}
                              className={`p-4 border rounded-lg ${isEvalUnlocked ? "hover:bg-gray-50" : "bg-gray-50"} transition-colors`}
                            >
                              <div className="flex items-center justify-between mb-2">
                                <h4 className="font-medium text-gray-900 flex items-center gap-2">
                                  {evaluation.title}
                                  {isCompleted && <CheckCircle className="h-4 w-4 text-green-600" />}
                                  {!isEvalUnlocked && <Lock className="h-4 w-4 text-gray-400" />}
                                </h4>
                                <Badge className={getDifficultyColor(evaluation.difficulty)} variant="outline">
                                  {evaluation.difficulty}
                                </Badge>
                              </div>
                              <p className="text-sm text-gray-600 mb-3">{evaluation.description}</p>
                              <div className="flex justify-between items-center">
                                <div className="flex items-center gap-4 text-sm text-gray-500">
                                  <span className="flex items-center">
                                    <Clock className="h-4 w-4 mr-1" />
                                    {evaluation.duration} min
                                  </span>
                                  <span>{evaluation.questions} preguntas</span>
                                </div>
                                {isEvalUnlocked && !isCompleted && (
                                  <Button
                                    size="sm"
                                    onClick={() => handleStartEvaluation(evaluation)}
                                    className="bg-blue-600 hover:bg-blue-700"
                                  >
                                    <Play className="h-4 w-4 mr-1" />
                                    Iniciar
                                  </Button>
                                )}
                                {isCompleted && (
                                  <Badge className="bg-green-100 text-green-800 border-green-200">
                                    <Award className="h-3 w-3 mr-1" />
                                    Completado
                                  </Badge>
                                )}
                                {!isEvalUnlocked && (
                                  <Badge variant="outline" className="text-gray-500">
                                    Bloqueado
                                  </Badge>
                                )}
                              </div>
                            </div>
                          )
                        })}
                      </div>
                    </CardContent>
                  </Card>
                )
              })}
            </div>
          </TabsContent>

          {/* Evaluations Tab */}
          <TabsContent value="evaluations" className="space-y-6">
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
              <div>
                <h2 className="text-2xl font-bold text-gray-900">Evaluaciones Disponibles</h2>
                <p className="text-gray-600 mt-1">Selecciona una evaluación para comenzar tu certificación</p>
              </div>
              {userRole === "ADMIN" && (
                <Button
                  onClick={() => {
                    setSelectedEvaluation(null)
                    setIsEditingEvaluation(false)
                    setActiveTab("admin")
                  }}
                >
                  <Plus className="h-4 w-4 mr-2" />
                  Nueva Evaluación
                </Button>
              )}
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
              {evaluations.map((evaluation) => {
                const isUnlocked = isEvaluationUnlocked(evaluation)
                const isCompleted = userResults.some((r) => r.evaluationId === evaluation.id && r.certified)

                return (
                  <Card
                    key={evaluation.id}
                    className={`hover:shadow-lg transition-all duration-200 border-0 shadow-md ${!isUnlocked ? "opacity-75" : ""}`}
                  >
                    <CardHeader className="pb-4">
                      <div className="flex justify-between items-start mb-3">
                        <div className="flex-1">
                          <CardTitle className="text-lg text-gray-900 mb-2 flex items-center gap-2">
                            {evaluation.title}
                            {isCompleted && <CheckCircle className="h-5 w-5 text-green-600" />}
                            {!isUnlocked && <Lock className="h-5 w-5 text-gray-400" />}
                          </CardTitle>
                          <CardDescription className="text-gray-600 line-clamp-2">
                            {evaluation.description}
                          </CardDescription>
                        </div>
                        {userRole === "ADMIN" && (
                          <div className="flex space-x-1 ml-2">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleEditEvaluation(evaluation)}
                              className="h-8 w-8 p-0"
                            >
                              <Edit className="h-4 w-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleDeleteEvaluation(evaluation.id)}
                              className="h-8 w-8 p-0 text-red-600 hover:text-red-700"
                            >
                              <Trash2 className="h-4 w-4" />
                            </Button>
                          </div>
                        )}
                      </div>

                      <div className="flex flex-wrap gap-2">
                        <Badge className="bg-blue-100 text-blue-800 border-blue-200" variant="outline">
                          {evaluation.category}
                        </Badge>
                        <Badge className={getDifficultyColor(evaluation.difficulty)} variant="outline">
                          {evaluation.difficulty}
                        </Badge>
                        <Badge className={getStatusColor(evaluation.status)} variant="outline">
                          {evaluation.status}
                        </Badge>
                      </div>
                    </CardHeader>

                    <CardContent className="pt-0">
                      <div className="space-y-3">
                        <div className="flex justify-between items-center text-sm text-gray-600">
                          <span className="flex items-center">
                            <Clock className="h-4 w-4 mr-1" />
                            {evaluation.duration} min
                          </span>
                          <span>{evaluation.questions} preguntas</span>
                        </div>

                        <div className="text-sm text-gray-600">
                          <span className="font-medium">{evaluation.completions}</span> personas han completado esta
                          evaluación
                        </div>

                        {evaluation.prerequisiteEvaluationId && (
                          <div className="text-xs text-gray-500 bg-gray-50 p-2 rounded">
                            <span className="font-medium">Prerequisito:</span> Completar evaluación anterior
                          </div>
                        )}

                        {userRole === "NORMAL" && evaluation.status === "Activa" && (
                          <>
                            {isUnlocked && !isCompleted && (
                              <Button className="w-full mt-4" onClick={() => handleStartEvaluation(evaluation)}>
                                <Play className="h-4 w-4 mr-2" />
                                Iniciar Evaluación
                              </Button>
                            )}
                            {!isUnlocked && (
                              <Button className="w-full mt-4" disabled>
                                <Lock className="h-4 w-4 mr-2" />
                                Evaluación Bloqueada
                              </Button>
                            )}
                            {isCompleted && (
                              <Button className="w-full mt-4 bg-transparent" variant="outline" disabled>
                                <CheckCircle className="h-4 w-4 mr-2" />
                                Evaluación Completada
                              </Button>
                            )}
                          </>
                        )}

                        {userRole === "ADMIN" && (
                          <div className="flex gap-2 mt-4">
                            <Button
                              variant="outline"
                              size="sm"
                              className="flex-1 bg-transparent"
                              onClick={() => handleEditEvaluation(evaluation)}
                            >
                              <Edit className="h-4 w-4 mr-1" />
                              Editar
                            </Button>
                            <Button
                              variant={evaluation.status === "Activa" ? "secondary" : "default"}
                              size="sm"
                              className="flex-1"
                            >
                              {evaluation.status === "Activa" ? "Desactivar" : "Activar"}
                            </Button>
                          </div>
                        )}
                      </div>
                    </CardContent>
                  </Card>
                )
              })}
            </div>

            {evaluations.length === 0 && (
              <div className="text-center py-12">
                <BookOpen className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                <h3 className="text-lg font-medium text-gray-900 mb-2">No hay evaluaciones disponibles</h3>
                <p className="text-gray-600">
                  {userRole === "ADMIN"
                    ? "Crea tu primera evaluación para comenzar."
                    : "Las evaluaciones estarán disponibles pronto."}
                </p>
              </div>
            )}
          </TabsContent>

          {/* Admin Panel */}
          {userRole === "ADMIN" && (
            <>
              <TabsContent value="admin">
                <AdminPanel
                  evaluations={evaluations}
                  categories={categories}
                  selectedEvaluation={isEditingEvaluation ? selectedEvaluation : null}
                  onClose={() => {
                    setIsEditingEvaluation(false)
                    setSelectedEvaluation(null)
                  }}
                  onCreateEvaluation={handleCreateEvaluation}
                  onUpdateEvaluation={handleUpdateEvaluation}
                  onDeleteEvaluation={handleDeleteEvaluation}
                />
              </TabsContent>

              <TabsContent value="centers">
                <CentersManagement />
              </TabsContent>

              <TabsContent value="students">
                <StudentsManagement />
              </TabsContent>
            </>
          )}

          {/* Evaluation Taker */}
          {selectedEvaluation && (
            <TabsContent value="take-evaluation">
              <EvaluationTaker
                evaluation={selectedEvaluation}
                onComplete={handleEvaluationComplete}
                onCancel={() => {
                  setSelectedEvaluation(null)
                  setActiveTab("evaluations")
                }}
              />
            </TabsContent>
          )}
        </Tabs>
      </div>

      {/* Chatbot */}
      {showChatbot && (
        <div className="fixed bottom-4 right-4 z-50">
          <Chatbot onClose={() => setShowChatbot(false)} />
        </div>
      )}
    </div>
  )
}
