"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { Label } from "@/components/ui/label"
import { Progress } from "@/components/ui/progress"
import { Badge } from "@/components/ui/badge"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Clock, CheckCircle, AlertCircle, Award, ArrowLeft, ArrowRight, Flag, Eye } from "lucide-react"

interface Question {
  id: number
  text: string
  type: "multiple-choice" | "true-false"
  options?: string[]
  correctAnswer: string | number
  points: number
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

interface EvaluationTakerProps {
  evaluation: Evaluation
  onComplete: (result: any) => void
  onCancel: () => void
}

export default function EvaluationTaker({ evaluation, onComplete, onCancel }: EvaluationTakerProps) {
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0)
  const [answers, setAnswers] = useState<{ [key: number]: string | number }>({})
  const [timeLeft, setTimeLeft] = useState(evaluation.duration * 60) // en segundos
  const [isStarted, setIsStarted] = useState(false)
  const [isCompleted, setIsCompleted] = useState(false)
  const [showResults, setShowResults] = useState(false)
  const [showConfirmDialog, setShowConfirmDialog] = useState(false)
  const [flaggedQuestions, setFlaggedQuestions] = useState<Set<number>>(new Set())
  const [showReview, setShowReview] = useState(false)

  // Preguntas de ejemplo para la evaluación
  const [questions] = useState<Question[]>([
    {
      id: 1,
      text: "¿Cuál es la diferencia principal entre let y var en JavaScript?",
      type: "multiple-choice",
      options: [
        "No hay diferencia significativa",
        "let tiene scope de bloque, var tiene scope de función",
        "var es más moderno que let",
        "let solo funciona en modo estricto",
      ],
      correctAnswer: 1,
      points: 5,
    },
    {
      id: 2,
      text: "¿JavaScript es un lenguaje de programación orientado a objetos?",
      type: "true-false",
      options: ["Verdadero", "Falso"],
      correctAnswer: 0,
      points: 3,
    },
    {
      id: 3,
      text: "¿Cuál de los siguientes métodos se usa para agregar un elemento al final de un array?",
      type: "multiple-choice",
      options: ["push()", "pop()", "shift()", "unshift()"],
      correctAnswer: 0,
      points: 4,
    },
    {
      id: 4,
      text: "¿El operador === compara tanto valor como tipo de dato?",
      type: "true-false",
      options: ["Verdadero", "Falso"],
      correctAnswer: 0,
      points: 3,
    },
    {
      id: 5,
      text: "¿Cuál es la forma correcta de declarar una función en JavaScript?",
      type: "multiple-choice",
      options: ["function myFunction() {}", "def myFunction() {}", "func myFunction() {}", "method myFunction() {}"],
      correctAnswer: 0,
      points: 4,
    },
    {
      id: 6,
      text: "¿JavaScript puede ejecutarse tanto en el navegador como en el servidor?",
      type: "true-false",
      options: ["Verdadero", "Falso"],
      correctAnswer: 0,
      points: 3,
    },
    {
      id: 7,
      text: "¿Qué método se utiliza para convertir un string a número entero?",
      type: "multiple-choice",
      options: ["parseInt()", "parseFloat()", "Number()", "toString()"],
      correctAnswer: 0,
      points: 4,
    },
    {
      id: 8,
      text: "¿Los arrays en JavaScript pueden contener elementos de diferentes tipos?",
      type: "true-false",
      options: ["Verdadero", "Falso"],
      correctAnswer: 0,
      points: 3,
    },
  ])

  const currentQuestion = questions[currentQuestionIndex]
  const progress = ((currentQuestionIndex + 1) / questions.length) * 100
  const answeredQuestions = Object.keys(answers).length

  // Timer effect
  useEffect(() => {
    if (isStarted && !isCompleted && timeLeft > 0) {
      const timer = setInterval(() => {
        setTimeLeft((prev) => {
          if (prev <= 1) {
            handleTimeUp()
            return 0
          }
          return prev - 1
        })
      }, 1000)

      return () => clearInterval(timer)
    }
  }, [isStarted, isCompleted, timeLeft])

  const formatTime = (seconds: number) => {
    const minutes = Math.floor(seconds / 60)
    const remainingSeconds = seconds % 60
    return `${minutes.toString().padStart(2, "0")}:${remainingSeconds.toString().padStart(2, "0")}`
  }

  const handleStart = () => {
    setIsStarted(true)
  }

  const handleAnswer = (value: string | number) => {
    setAnswers({ ...answers, [currentQuestion.id]: value })
  }

  const handleNext = () => {
    if (currentQuestionIndex < questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1)
    }
  }

  const handlePrevious = () => {
    if (currentQuestionIndex > 0) {
      setCurrentQuestionIndex(currentQuestionIndex - 1)
    }
  }

  const handleFlag = () => {
    const newFlagged = new Set(flaggedQuestions)
    if (newFlagged.has(currentQuestion.id)) {
      newFlagged.delete(currentQuestion.id)
    } else {
      newFlagged.add(currentQuestion.id)
    }
    setFlaggedQuestions(newFlagged)
  }

  const handleTimeUp = () => {
    setIsCompleted(true)
    calculateResults()
  }

  const handleSubmit = () => {
    setShowConfirmDialog(true)
  }

  const confirmSubmit = () => {
    setIsCompleted(true)
    setShowConfirmDialog(false)
    calculateResults()
  }

  const calculateResults = () => {
    let correctAnswers = 0
    let totalPoints = 0
    let earnedPoints = 0

    questions.forEach((question) => {
      totalPoints += question.points
      const userAnswer = answers[question.id]
      if (userAnswer !== undefined && userAnswer === question.correctAnswer) {
        correctAnswers++
        earnedPoints += question.points
      }
    })

    const percentage = Math.round((earnedPoints / totalPoints) * 100)
    const passed = percentage >= evaluation.passingScore
    const timeSpent = evaluation.duration - Math.floor(timeLeft / 60)

    const result = {
      score: earnedPoints,
      maxScore: totalPoints,
      percentage,
      correctAnswers,
      totalQuestions: questions.length,
      certified: passed,
      timeSpent,
      answers,
      questions,
    }

    setTimeout(() => {
      setShowResults(true)
      onComplete(result)
    }, 1000)
  }

  const getTimeColor = () => {
    const percentage = (timeLeft / (evaluation.duration * 60)) * 100
    if (percentage <= 10) return "text-red-600"
    if (percentage <= 25) return "text-yellow-600"
    return "text-green-600"
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

  if (!isStarted) {
    return (
      <div className="max-w-4xl mx-auto space-y-6">
        <Card className="border-2 border-blue-200">
          <CardHeader className="text-center">
            <div className="mx-auto w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-4">
              <CheckCircle className="h-8 w-8 text-blue-600" />
            </div>
            <CardTitle className="text-2xl">{evaluation.title}</CardTitle>
            <CardDescription className="text-lg">{evaluation.description}</CardDescription>
          </CardHeader>
          <CardContent className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-center">
              <div className="p-4 bg-gray-50 rounded-lg">
                <Clock className="h-6 w-6 mx-auto mb-2 text-blue-600" />
                <p className="font-medium">Duración</p>
                <p className="text-sm text-gray-600">{evaluation.duration} minutos</p>
              </div>
              <div className="p-4 bg-gray-50 rounded-lg">
                <CheckCircle className="h-6 w-6 mx-auto mb-2 text-green-600" />
                <p className="font-medium">Preguntas</p>
                <p className="text-sm text-gray-600">{questions.length} preguntas</p>
              </div>
              <div className="p-4 bg-gray-50 rounded-lg">
                <Award className="h-6 w-6 mx-auto mb-2 text-purple-600" />
                <p className="font-medium">Puntuación Mínima</p>
                <p className="text-sm text-gray-600">{evaluation.passingScore}%</p>
              </div>
            </div>

            <div className="flex justify-center">
              <Badge className={getDifficultyColor(evaluation.difficulty)} variant="outline">
                Nivel: {evaluation.difficulty}
              </Badge>
            </div>

            <Alert>
              <AlertCircle className="h-4 w-4" />
              <AlertDescription>
                <strong>Instrucciones importantes:</strong>
                <ul className="mt-2 space-y-1 text-sm">
                  <li>• Tienes {evaluation.duration} minutos para completar la evaluación</li>
                  <li>• Puedes navegar entre preguntas y cambiar tus respuestas</li>
                  <li>• Marca las preguntas que quieras revisar más tarde</li>
                  <li>• La evaluación se enviará automáticamente cuando se acabe el tiempo</li>
                  <li>• Necesitas {evaluation.passingScore}% para obtener el certificado</li>
                </ul>
              </AlertDescription>
            </Alert>

            <div className="flex justify-center space-x-4">
              <Button variant="outline" onClick={onCancel}>
                <ArrowLeft className="h-4 w-4 mr-2" />
                Cancelar
              </Button>
              <Button onClick={handleStart} className="bg-blue-600 hover:bg-blue-700">
                <CheckCircle className="h-4 w-4 mr-2" />
                Comenzar Evaluación
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
    )
  }

  if (showResults) {
    const result = {
      score: 0,
      maxScore: questions.reduce((acc, q) => acc + q.points, 0),
      percentage: 0,
      correctAnswers: 0,
      totalQuestions: questions.length,
      certified: false,
      timeSpent: evaluation.duration - Math.floor(timeLeft / 60),
    }

    // Calcular resultados reales
    let correctAnswers = 0
    let earnedPoints = 0
    questions.forEach((question) => {
      const userAnswer = answers[question.id]
      if (userAnswer !== undefined && userAnswer === question.correctAnswer) {
        correctAnswers++
        earnedPoints += question.points
      }
    })

    result.score = earnedPoints
    result.percentage = Math.round((earnedPoints / result.maxScore) * 100)
    result.correctAnswers = correctAnswers
    result.certified = result.percentage >= evaluation.passingScore

    return (
      <div className="max-w-4xl mx-auto space-y-6">
        <Card className={`border-2 ${result.certified ? "border-green-200" : "border-red-200"}`}>
          <CardHeader className="text-center">
            <div
              className={`mx-auto w-20 h-20 rounded-full flex items-center justify-center mb-4 ${
                result.certified ? "bg-green-100" : "bg-red-100"
              }`}
            >
              {result.certified ? (
                <Award className="h-10 w-10 text-green-600" />
              ) : (
                <AlertCircle className="h-10 w-10 text-red-600" />
              )}
            </div>
            <CardTitle className="text-2xl">
              {result.certified ? "¡Felicitaciones!" : "Evaluación Completada"}
            </CardTitle>
            <CardDescription className="text-lg">
              {result.certified
                ? "Has aprobado la evaluación y obtenido tu certificado"
                : "No alcanzaste la puntuación mínima requerida"}
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4 text-center">
              <div className="p-4 bg-gray-50 rounded-lg">
                <p className="text-2xl font-bold text-blue-600">{result.percentage}%</p>
                <p className="text-sm text-gray-600">Puntuación Final</p>
              </div>
              <div className="p-4 bg-gray-50 rounded-lg">
                <p className="text-2xl font-bold text-green-600">{result.correctAnswers}</p>
                <p className="text-sm text-gray-600">Respuestas Correctas</p>
              </div>
              <div className="p-4 bg-gray-50 rounded-lg">
                <p className="text-2xl font-bold text-purple-600">{result.score}</p>
                <p className="text-sm text-gray-600">Puntos Obtenidos</p>
              </div>
              <div className="p-4 bg-gray-50 rounded-lg">
                <p className="text-2xl font-bold text-orange-600">{result.timeSpent}</p>
                <p className="text-sm text-gray-600">Minutos Utilizados</p>
              </div>
            </div>

            {result.certified && (
              <Alert className="bg-green-50 border-green-200">
                <Award className="h-4 w-4" />
                <AlertDescription className="text-green-800">
                  <strong>¡Certificado obtenido!</strong> Tu certificado digital ya está disponible en tu perfil. Puedes
                  descargarlo e incluirlo en tu CV o LinkedIn.
                </AlertDescription>
              </Alert>
            )}

            {!result.certified && (
              <Alert className="bg-red-50 border-red-200">
                <AlertCircle className="h-4 w-4" />
                <AlertDescription className="text-red-800">
                  <strong>Puntuación insuficiente.</strong> Necesitas {evaluation.passingScore}% para obtener el
                  certificado. Puedes intentar nuevamente después de 24 horas.
                </AlertDescription>
              </Alert>
            )}

            <div className="flex justify-center space-x-4">
              <Button variant="outline" onClick={() => setShowReview(true)}>
                <Eye className="h-4 w-4 mr-2" />
                Revisar Respuestas
              </Button>
              <Button onClick={onCancel} className="bg-blue-600 hover:bg-blue-700">
                <ArrowLeft className="h-4 w-4 mr-2" />
                Volver al Dashboard
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
    )
  }

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* Header with timer and progress */}
      <Card>
        <CardContent className="p-4">
          <div className="flex justify-between items-center mb-4">
            <div>
              <h2 className="text-xl font-semibold">{evaluation.title}</h2>
              <p className="text-sm text-gray-600">
                Pregunta {currentQuestionIndex + 1} de {questions.length}
              </p>
            </div>
            <div className="text-right">
              <div className={`text-2xl font-bold ${getTimeColor()}`}>
                <Clock className="h-5 w-5 inline mr-2" />
                {formatTime(timeLeft)}
              </div>
              <p className="text-sm text-gray-600">Tiempo restante</p>
            </div>
          </div>
          <Progress value={progress} className="h-2" />
          <div className="flex justify-between text-sm text-gray-600 mt-2">
            <span>Progreso: {Math.round(progress)}%</span>
            <span>
              Respondidas: {answeredQuestions}/{questions.length}
            </span>
          </div>
        </CardContent>
      </Card>

      {/* Question Card */}
      <Card>
        <CardHeader>
          <div className="flex justify-between items-start">
            <div className="flex-1">
              <CardTitle className="text-lg mb-2">
                Pregunta {currentQuestionIndex + 1}
                {flaggedQuestions.has(currentQuestion.id) && <Flag className="h-4 w-4 inline ml-2 text-yellow-600" />}
              </CardTitle>
              <CardDescription className="text-base">{currentQuestion.text}</CardDescription>
            </div>
            <div className="flex items-center space-x-2">
              <Badge variant="outline">{currentQuestion.points} pts</Badge>
              <Button
                variant="ghost"
                size="sm"
                onClick={handleFlag}
                className={flaggedQuestions.has(currentQuestion.id) ? "text-yellow-600" : ""}
              >
                <Flag className="h-4 w-4" />
              </Button>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <RadioGroup
            value={answers[currentQuestion.id]?.toString() || ""}
            onValueChange={(value) => handleAnswer(Number.parseInt(value))}
          >
            {currentQuestion.options?.map((option, index) => (
              <div key={index} className="flex items-center space-x-2 p-3 border rounded-lg hover:bg-gray-50">
                <RadioGroupItem value={index.toString()} id={`option-${index}`} />
                <Label htmlFor={`option-${index}`} className="flex-1 cursor-pointer">
                  {option}
                </Label>
              </div>
            ))}
          </RadioGroup>
        </CardContent>
      </Card>

      {/* Navigation */}
      <Card>
        <CardContent className="p-4">
          <div className="flex justify-between items-center">
            <Button variant="outline" onClick={handlePrevious} disabled={currentQuestionIndex === 0}>
              <ArrowLeft className="h-4 w-4 mr-2" />
              Anterior
            </Button>

            <div className="flex space-x-2">
              {flaggedQuestions.size > 0 && (
                <Badge variant="outline" className="text-yellow-600 border-yellow-600">
                  <Flag className="h-3 w-3 mr-1" />
                  {flaggedQuestions.size} marcadas
                </Badge>
              )}
              <Button variant="outline" onClick={() => setShowReview(true)}>
                <Eye className="h-4 w-4 mr-2" />
                Revisar
              </Button>
            </div>

            {currentQuestionIndex === questions.length - 1 ? (
              <Button onClick={handleSubmit} className="bg-green-600 hover:bg-green-700">
                <CheckCircle className="h-4 w-4 mr-2" />
                Finalizar
              </Button>
            ) : (
              <Button onClick={handleNext}>
                Siguiente
                <ArrowRight className="h-4 w-4 ml-2" />
              </Button>
            )}
          </div>
        </CardContent>
      </Card>

      {/* Confirmation Dialog */}
      <Dialog open={showConfirmDialog} onOpenChange={setShowConfirmDialog}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>¿Finalizar Evaluación?</DialogTitle>
            <DialogDescription>
              Estás a punto de enviar tu evaluación. Una vez enviada, no podrás modificar tus respuestas.
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            <div className="p-4 bg-gray-50 rounded-lg">
              <p className="text-sm">
                <strong>Resumen:</strong>
              </p>
              <ul className="text-sm text-gray-600 mt-2">
                <li>
                  • Preguntas respondidas: {answeredQuestions} de {questions.length}
                </li>
                <li>• Preguntas marcadas: {flaggedQuestions.size}</li>
                <li>• Tiempo restante: {formatTime(timeLeft)}</li>
              </ul>
            </div>
            <div className="flex justify-end space-x-2">
              <Button variant="outline" onClick={() => setShowConfirmDialog(false)}>
                Continuar Evaluación
              </Button>
              <Button onClick={confirmSubmit} className="bg-green-600 hover:bg-green-700">
                Enviar Evaluación
              </Button>
            </div>
          </div>
        </DialogContent>
      </Dialog>

      {/* Review Dialog */}
      <Dialog open={showReview} onOpenChange={setShowReview}>
        <DialogContent className="max-w-2xl max-h-[80vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle>Revisar Respuestas</DialogTitle>
            <DialogDescription>Revisa tus respuestas antes de finalizar la evaluación</DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            {questions.map((question, index) => {
              const isAnswered = answers[question.id] !== undefined
              const isFlagged = flaggedQuestions.has(question.id)

              return (
                <div key={question.id} className="p-4 border rounded-lg">
                  <div className="flex justify-between items-start mb-2">
                    <h4 className="font-medium">Pregunta {index + 1}</h4>
                    <div className="flex space-x-2">
                      {isFlagged && <Flag className="h-4 w-4 text-yellow-600" />}
                      {isAnswered ? (
                        <CheckCircle className="h-4 w-4 text-green-600" />
                      ) : (
                        <AlertCircle className="h-4 w-4 text-red-600" />
                      )}
                    </div>
                  </div>
                  <p className="text-sm text-gray-700 mb-2">{question.text}</p>
                  {isAnswered && (
                    <p className="text-sm font-medium text-blue-600">
                      Respuesta: {question.options?.[answers[question.id] as number]}
                    </p>
                  )}
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => {
                      setCurrentQuestionIndex(index)
                      setShowReview(false)
                    }}
                    className="mt-2"
                  >
                    Ir a pregunta
                  </Button>
                </div>
              )
            })}
          </div>
        </DialogContent>
      </Dialog>
    </div>
  )
}
