"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Badge } from "@/components/ui/badge"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { Plus, Save, X, Edit, Trash2, Eye, Users, BarChart3 } from "lucide-react"

interface Question {
  id: number
  text: string
  type: "multiple-choice" | "true-false" | "open"
  options?: string[]
  correctAnswer: string | number
  points: number
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
  createdAt?: string
  updatedAt?: string
  prerequisiteEvaluationId?: number
  passingScore: number
}

interface AdminPanelProps {
  evaluations: Evaluation[]
  categories: Category[]
  selectedEvaluation?: Evaluation | null
  onClose: () => void
  onCreateEvaluation: (evaluation: Omit<Evaluation, "id" | "completions" | "createdAt" | "updatedAt">) => void
  onUpdateEvaluation: (evaluation: Evaluation) => void
  onDeleteEvaluation: (id: number) => void
}

export default function AdminPanel({
  evaluations,
  categories,
  selectedEvaluation,
  onClose,
  onCreateEvaluation,
  onUpdateEvaluation,
  onDeleteEvaluation,
}: AdminPanelProps) {
  const [activeTab, setActiveTab] = useState("create")
  const [formData, setFormData] = useState({
    title: selectedEvaluation?.title || "",
    description: selectedEvaluation?.description || "",
    categoryId: selectedEvaluation?.categoryId || 0,
    duration: selectedEvaluation?.duration || 30,
    difficulty: selectedEvaluation?.difficulty || "Básico",
    status: selectedEvaluation?.status || "Inactiva",
    prerequisiteEvaluationId: selectedEvaluation?.prerequisiteEvaluationId || 0,
    passingScore: selectedEvaluation?.passingScore || 70,
  })

  const [questions, setQuestions] = useState<Question[]>([
    {
      id: 1,
      text: "¿Cuál es la diferencia entre let y var en JavaScript?",
      type: "multiple-choice",
      options: [
        "No hay diferencia",
        "let tiene scope de bloque, var tiene scope de función",
        "var es más moderno que let",
        "let solo se puede usar en funciones",
      ],
      correctAnswer: 1,
      points: 5,
    },
  ])

  const [currentQuestion, setCurrentQuestion] = useState<Question>({
    id: 0,
    text: "",
    type: "multiple-choice",
    options: ["", "", "", ""],
    correctAnswer: 0,
    points: 5,
  })

  const [showSuccess, setShowSuccess] = useState(false)

  const handleSaveEvaluation = () => {
    const selectedCategory = categories.find((c) => c.id === formData.categoryId)

    if (selectedEvaluation) {
      // Actualizar evaluación existente
      const updatedEvaluation: Evaluation = {
        ...selectedEvaluation,
        ...formData,
        category: selectedCategory?.name || "",
        updatedAt: new Date().toISOString(),
      }
      onUpdateEvaluation(updatedEvaluation)
    } else {
      // Crear nueva evaluación
      onCreateEvaluation({
        ...formData,
        category: selectedCategory?.name || "",
        questions: questions.length,
      })
    }
    setShowSuccess(true)
    setTimeout(() => {
      setShowSuccess(false)
      onClose()
    }, 2000)
  }

  const handleAddQuestion = () => {
    if (currentQuestion.text.trim()) {
      const newQuestion = {
        ...currentQuestion,
        id: Date.now(),
      }
      setQuestions([...questions, newQuestion])
      setCurrentQuestion({
        id: 0,
        text: "",
        type: "multiple-choice",
        options: ["", "", "", ""],
        correctAnswer: 0,
        points: 5,
      })
    }
  }

  const handleDeleteQuestion = (id: number) => {
    setQuestions(questions.filter((q) => q.id !== id))
  }

  const handleEditQuestion = (question: Question) => {
    setCurrentQuestion(question)
    setQuestions(questions.filter((q) => q.id !== question.id))
  }

  const getAvailablePrerequisites = () => {
    if (!formData.categoryId) return []

    return evaluations.filter((e) => e.categoryId === formData.categoryId && e.id !== selectedEvaluation?.id)
  }

  useEffect(() => {
    if (selectedEvaluation) {
      setFormData({
        title: selectedEvaluation.title,
        description: selectedEvaluation.description,
        categoryId: selectedEvaluation.categoryId,
        duration: selectedEvaluation.duration,
        difficulty: selectedEvaluation.difficulty,
        status: selectedEvaluation.status,
        prerequisiteEvaluationId: selectedEvaluation.prerequisiteEvaluationId || 0,
        passingScore: selectedEvaluation.passingScore,
      })
    }
  }, [selectedEvaluation])

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">{selectedEvaluation ? "Editar Evaluación" : "Panel de Administración"}</h2>
        {selectedEvaluation && (
          <Button variant="outline" onClick={onClose}>
            <X className="h-4 w-4 mr-2" />
            Cerrar
          </Button>
        )}
      </div>

      {showSuccess && (
        <Alert className="bg-green-50 border-green-200">
          <AlertDescription className="text-green-800">¡Evaluación guardada exitosamente!</AlertDescription>
        </Alert>
      )}

      <Tabs value={activeTab} onValueChange={setActiveTab}>
        <TabsList className="grid w-full grid-cols-3">
          <TabsTrigger value="create">{selectedEvaluation ? "Editar" : "Crear"} Evaluación</TabsTrigger>
          <TabsTrigger value="questions">Gestionar Preguntas</TabsTrigger>
          <TabsTrigger value="analytics">Estadísticas</TabsTrigger>
        </TabsList>

        {/* Create/Edit Evaluation */}
        <TabsContent value="create" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Información de la Evaluación</CardTitle>
              <CardDescription>Configura los detalles básicos de la evaluación</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="title">Título</Label>
                  <Input
                    id="title"
                    value={formData.title}
                    onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                    placeholder="Ej: Fundamentos de JavaScript"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="category">Categoría</Label>
                  <Select
                    value={formData.categoryId.toString()}
                    onValueChange={(value) => setFormData({ ...formData, categoryId: Number.parseInt(value) })}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Selecciona una categoría" />
                    </SelectTrigger>
                    <SelectContent>
                      {categories.map((category) => (
                        <SelectItem key={category.id} value={category.id.toString()}>
                          {category.icon} {category.name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="description">Descripción</Label>
                <Textarea
                  id="description"
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  placeholder="Describe qué evalúa esta prueba..."
                  rows={3}
                />
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="prerequisite">Evaluación Prerequisito</Label>
                  <Select
                    value={formData.prerequisiteEvaluationId?.toString() || "0"}
                    onValueChange={(value) =>
                      setFormData({
                        ...formData,
                        prerequisiteEvaluationId: value === "0" ? undefined : Number.parseInt(value),
                      })
                    }
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Sin prerequisito" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="0">Sin prerequisito</SelectItem>
                      {getAvailablePrerequisites().map((evaluation) => (
                        <SelectItem key={evaluation.id} value={evaluation.id.toString()}>
                          {evaluation.title}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="passingScore">Puntuación Mínima (%)</Label>
                  <Input
                    id="passingScore"
                    type="number"
                    value={formData.passingScore}
                    onChange={(e) => setFormData({ ...formData, passingScore: Number.parseInt(e.target.value) })}
                    min="50"
                    max="100"
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="duration">Duración (minutos)</Label>
                  <Input
                    id="duration"
                    type="number"
                    value={formData.duration}
                    onChange={(e) => setFormData({ ...formData, duration: Number.parseInt(e.target.value) })}
                    min="10"
                    max="180"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="difficulty">Dificultad</Label>
                  <Select
                    value={formData.difficulty}
                    onValueChange={(value: any) => setFormData({ ...formData, difficulty: value })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="Básico">Básico</SelectItem>
                      <SelectItem value="Intermedio">Intermedio</SelectItem>
                      <SelectItem value="Avanzado">Avanzado</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="status">Estado</Label>
                  <Select
                    value={formData.status}
                    onValueChange={(value: any) => setFormData({ ...formData, status: value })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="Activa">Activa</SelectItem>
                      <SelectItem value="Inactiva">Inactiva</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>

              <Button onClick={handleSaveEvaluation} className="w-full">
                <Save className="h-4 w-4 mr-2" />
                {selectedEvaluation ? "Actualizar" : "Crear"} Evaluación
              </Button>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Questions Management */}
        <TabsContent value="questions" className="space-y-6">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* Add Question Form */}
            <Card>
              <CardHeader>
                <CardTitle>Agregar Nueva Pregunta</CardTitle>
                <CardDescription>Crea preguntas para la evaluación</CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="question-text">Pregunta</Label>
                  <Textarea
                    id="question-text"
                    value={currentQuestion.text}
                    onChange={(e) => setCurrentQuestion({ ...currentQuestion, text: e.target.value })}
                    placeholder="Escribe tu pregunta aquí..."
                    rows={3}
                  />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="question-type">Tipo</Label>
                    <Select
                      value={currentQuestion.type}
                      onValueChange={(value: any) => setCurrentQuestion({ ...currentQuestion, type: value })}
                    >
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="multiple-choice">Opción Múltiple</SelectItem>
                        <SelectItem value="true-false">Verdadero/Falso</SelectItem>
                        <SelectItem value="open">Respuesta Abierta</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="points">Puntos</Label>
                    <Input
                      id="points"
                      type="number"
                      value={currentQuestion.points}
                      onChange={(e) =>
                        setCurrentQuestion({ ...currentQuestion, points: Number.parseInt(e.target.value) })
                      }
                      min="1"
                      max="20"
                    />
                  </div>
                </div>

                {currentQuestion.type === "multiple-choice" && (
                  <div className="space-y-2">
                    <Label>Opciones</Label>
                    {currentQuestion.options?.map((option, index) => (
                      <div key={index} className="flex items-center space-x-2">
                        <Input
                          value={option}
                          onChange={(e) => {
                            const newOptions = [...(currentQuestion.options || [])]
                            newOptions[index] = e.target.value
                            setCurrentQuestion({ ...currentQuestion, options: newOptions })
                          }}
                          placeholder={`Opción ${index + 1}`}
                        />
                        <input
                          type="radio"
                          name="correct-answer"
                          checked={currentQuestion.correctAnswer === index}
                          onChange={() => setCurrentQuestion({ ...currentQuestion, correctAnswer: index })}
                        />
                      </div>
                    ))}
                  </div>
                )}

                <Button onClick={handleAddQuestion} className="w-full">
                  <Plus className="h-4 w-4 mr-2" />
                  Agregar Pregunta
                </Button>
              </CardContent>
            </Card>

            {/* Questions List */}
            <Card>
              <CardHeader>
                <CardTitle>Preguntas de la Evaluación ({questions.length})</CardTitle>
                <CardDescription>Gestiona las preguntas existentes</CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-4 max-h-96 overflow-y-auto">
                  {questions.map((question, index) => (
                    <div key={question.id} className="p-4 border rounded-lg">
                      <div className="flex justify-between items-start mb-2">
                        <span className="text-sm font-medium">Pregunta {index + 1}</span>
                        <div className="flex space-x-1">
                          <Button variant="ghost" size="sm" onClick={() => handleEditQuestion(question)}>
                            <Edit className="h-3 w-3" />
                          </Button>
                          <Button variant="ghost" size="sm" onClick={() => handleDeleteQuestion(question.id)}>
                            <Trash2 className="h-3 w-3" />
                          </Button>
                        </div>
                      </div>
                      <p className="text-sm text-gray-700 mb-2">{question.text}</p>
                      <div className="flex justify-between items-center">
                        <Badge variant="outline">{question.type}</Badge>
                        <span className="text-sm text-gray-500">{question.points} pts</span>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        {/* Analytics */}
        <TabsContent value="analytics" className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Total Evaluaciones</CardTitle>
                <BarChart3 className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{evaluations.length}</div>
                <p className="text-xs text-muted-foreground">
                  {evaluations.filter((e) => e.status === "Activa").length} activas
                </p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Total Participantes</CardTitle>
                <Users className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{evaluations.reduce((acc, e) => acc + e.completions, 0)}</div>
                <p className="text-xs text-muted-foreground">Evaluaciones completadas</p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Promedio Completadas</CardTitle>
                <Eye className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">
                  {Math.round(evaluations.reduce((acc, e) => acc + e.completions, 0) / evaluations.length)}
                </div>
                <p className="text-xs text-muted-foreground">Por evaluación</p>
              </CardContent>
            </Card>
          </div>

          <Card>
            <CardHeader>
              <CardTitle>Evaluaciones por Categoría</CardTitle>
              <CardDescription>Distribución de evaluaciones por área de conocimiento</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {categories.map((category) => {
                  const categoryEvaluations = evaluations.filter((e) => e.categoryId === category.id)
                  const totalCompletions = categoryEvaluations.reduce((acc, e) => acc + e.completions, 0)

                  return (
                    <div key={category.id} className="flex items-center justify-between p-4 border rounded-lg">
                      <div className="flex items-center space-x-4">
                        <span className="text-2xl">{category.icon}</span>
                        <div>
                          <h4 className="font-medium">{category.name}</h4>
                          <p className="text-sm text-gray-500">{categoryEvaluations.length} evaluaciones</p>
                        </div>
                      </div>
                      <div className="text-right">
                        <p className="font-medium">{totalCompletions} completadas</p>
                        <Badge className={category.color} variant="outline">
                          {category.name}
                        </Badge>
                      </div>
                    </div>
                  )
                })}
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Evaluaciones Más Populares</CardTitle>
              <CardDescription>Ranking de evaluaciones por número de completadas</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {evaluations
                  .sort((a, b) => b.completions - a.completions)
                  .map((evaluation, index) => (
                    <div key={evaluation.id} className="flex items-center justify-between p-4 border rounded-lg">
                      <div className="flex items-center space-x-4">
                        <span className="text-2xl font-bold text-gray-400">#{index + 1}</span>
                        <div>
                          <h4 className="font-medium">{evaluation.title}</h4>
                          <p className="text-sm text-gray-500">{evaluation.category}</p>
                        </div>
                      </div>
                      <div className="text-right">
                        <p className="font-medium">{evaluation.completions} completadas</p>
                        <Badge variant="outline">{evaluation.difficulty}</Badge>
                      </div>
                    </div>
                  ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
