"use client"

import type React from "react"

import { useState, useRef, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { ScrollArea } from "@/components/ui/scroll-area"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"
import { X, Send, Bot, User, Lightbulb, HelpCircle, Minimize2, Maximize2 } from "lucide-react"

interface Message {
  id: number
  text: string
  sender: "user" | "bot"
  timestamp: Date
  type?: "text" | "suggestion" | "help"
}

interface ChatbotProps {
  onClose: () => void
}

export default function Chatbot({ onClose }: ChatbotProps) {
  const [messages, setMessages] = useState<Message[]>([
    {
      id: 1,
      text: "¡Hola! Soy tu asistente virtual de SkillCertify. ¿En qué puedo ayudarte hoy?",
      sender: "bot",
      timestamp: new Date(),
      type: "text",
    },
  ])
  const [inputMessage, setInputMessage] = useState("")
  const [isTyping, setIsTyping] = useState(false)
  const [isMinimized, setIsMinimized] = useState(false)
  const messagesEndRef = useRef<HTMLDivElement>(null)
  const inputRef = useRef<HTMLInputElement>(null)

  const quickSuggestions = [
    "¿Cómo puedo obtener un certificado?",
    "¿Cuáles son los requisitos para las evaluaciones?",
    "¿Cómo funciona el sistema de prerrequisitos?",
    "¿Dónde puedo ver mi progreso?",
    "¿Cómo contactar soporte técnico?",
  ]

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" })
  }

  useEffect(() => {
    scrollToBottom()
  }, [messages])

  const getBotResponse = (userMessage: string): string => {
    const message = userMessage.toLowerCase()

    // Respuestas específicas basadas en palabras clave
    if (message.includes("certificado") || message.includes("certificación")) {
      return "Para obtener un certificado, debes completar una evaluación con una puntuación mínima del 70%. Una vez aprobada, tu certificado digital estará disponible en tu perfil en unos minutos. Los certificados son reconocidos por más de 500 empresas aliadas en Perú."
    }

    if (message.includes("requisito") || message.includes("prerrequisito")) {
      return "Nuestro sistema tiene un orden de aprendizaje progresivo. Debes completar las evaluaciones en secuencia: primero los fundamentos, luego frontend, backend y finalmente full stack. Cada evaluación desbloqueará la siguiente una vez que obtengas la certificación."
    }

    if (message.includes("progreso") || message.includes("avance")) {
      return "Puedes ver tu progreso en la pestaña 'Ruta de Aprendizaje' del dashboard. Allí encontrarás tu avance por categorías, evaluaciones completadas, certificados obtenidos y tu puntuación promedio."
    }

    if (message.includes("evaluación") || message.includes("examen")) {
      return "Las evaluaciones tienen diferentes duraciones (30-60 minutos) y niveles de dificultad. Incluyen preguntas de opción múltiple y verdadero/falso. Puedes ver el tiempo restante durante la evaluación y revisar tus respuestas antes de enviar."
    }

    if (message.includes("soporte") || message.includes("ayuda") || message.includes("problema")) {
      return "Para soporte técnico, puedes contactarnos a través de: \n• Email: soporte@skillcertify.pe \n• WhatsApp: +51 999 888 777 \n• Horario: Lunes a Viernes 8:00 AM - 6:00 PM \n\nTambién puedes consultar nuestra base de conocimientos en el portal."
    }

    if (message.includes("precio") || message.includes("costo") || message.includes("pago")) {
      return "SkillCertify ofrece diferentes planes: \n• Plan Básico: Gratuito (evaluaciones básicas) \n• Plan Premium: S/. 99/mes (todas las evaluaciones) \n• Plan Empresarial: Contactar para cotización \n\nTodos los planes incluyen certificados digitales verificables."
    }

    if (message.includes("tiempo") || message.includes("duración")) {
      return "Los tiempos de evaluación varían según la complejidad: \n• Evaluaciones básicas: 30-45 minutos \n• Evaluaciones intermedias: 45-60 minutos \n• Evaluaciones avanzadas: 60-90 minutos \n\nPuedes pausar y continuar más tarde si es necesario."
    }

    if (message.includes("hola") || message.includes("buenos") || message.includes("buenas")) {
      return "¡Hola! Me da mucho gusto saludarte. Estoy aquí para ayudarte con cualquier pregunta sobre SkillCertify. ¿Hay algo específico en lo que pueda asistirte?"
    }

    if (message.includes("gracias") || message.includes("thank")) {
      return "¡De nada! Es un placer ayudarte. Si tienes más preguntas sobre SkillCertify, no dudes en consultarme. ¡Éxito en tu proceso de certificación! 🎓"
    }

    // Respuesta por defecto
    return "Entiendo tu consulta. Te recomiendo revisar las siguientes secciones: \n• Dashboard para ver tu progreso \n• Ruta de Aprendizaje para las evaluaciones disponibles \n• Configuración para gestionar tu perfil \n\n¿Hay algo más específico en lo que pueda ayudarte?"
  }

  const handleSendMessage = async () => {
    if (!inputMessage.trim()) return

    const userMessage: Message = {
      id: Date.now(),
      text: inputMessage,
      sender: "user",
      timestamp: new Date(),
      type: "text",
    }

    setMessages((prev) => [...prev, userMessage])
    setInputMessage("")
    setIsTyping(true)

    // Simular delay de respuesta del bot
    setTimeout(
      () => {
        const botResponse: Message = {
          id: Date.now() + 1,
          text: getBotResponse(inputMessage),
          sender: "bot",
          timestamp: new Date(),
          type: "text",
        }

        setMessages((prev) => [...prev, botResponse])
        setIsTyping(false)
      },
      1000 + Math.random() * 1000,
    ) // 1-2 segundos de delay
  }

  const handleSuggestionClick = (suggestion: string) => {
    setInputMessage(suggestion)
    inputRef.current?.focus()
  }

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault()
      handleSendMessage()
    }
  }

  if (isMinimized) {
    return (
      <Card className="w-80 h-16 shadow-lg border-2 border-blue-200">
        <CardContent className="p-3">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="bg-blue-600 p-1 rounded-full">
                <Bot className="h-4 w-4 text-white" />
              </div>
              <span className="font-medium text-sm">Asistente SkillCertify</span>
              <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></div>
            </div>
            <div className="flex gap-1">
              <Button variant="ghost" size="sm" onClick={() => setIsMinimized(false)} className="h-6 w-6 p-0">
                <Maximize2 className="h-3 w-3" />
              </Button>
              <Button variant="ghost" size="sm" onClick={onClose} className="h-6 w-6 p-0">
                <X className="h-3 w-3" />
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card className="w-96 h-[600px] shadow-xl border-2 border-blue-200 flex flex-col">
      <CardHeader className="pb-3 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-t-lg">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="bg-white/20 p-2 rounded-full">
              <Bot className="h-5 w-5" />
            </div>
            <div>
              <CardTitle className="text-lg">Asistente SkillCertify</CardTitle>
              <div className="flex items-center gap-2 text-blue-100 text-sm">
                <div className="w-2 h-2 bg-green-400 rounded-full animate-pulse"></div>
                En línea
              </div>
            </div>
          </div>
          <div className="flex gap-1">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => setIsMinimized(true)}
              className="h-8 w-8 p-0 text-white hover:bg-white/20"
            >
              <Minimize2 className="h-4 w-4" />
            </Button>
            <Button variant="ghost" size="sm" onClick={onClose} className="h-8 w-8 p-0 text-white hover:bg-white/20">
              <X className="h-4 w-4" />
            </Button>
          </div>
        </div>
      </CardHeader>

      <CardContent className="flex-1 flex flex-col p-0">
        {/* Messages Area */}
        <ScrollArea className="flex-1 p-4">
          <div className="space-y-4">
            {messages.map((message) => (
              <div
                key={message.id}
                className={`flex gap-3 ${message.sender === "user" ? "justify-end" : "justify-start"}`}
              >
                {message.sender === "bot" && (
                  <Avatar className="h-8 w-8 bg-blue-100">
                    <AvatarFallback>
                      <Bot className="h-4 w-4 text-blue-600" />
                    </AvatarFallback>
                  </Avatar>
                )}

                <div className={`max-w-[80%] ${message.sender === "user" ? "order-first" : ""}`}>
                  <div
                    className={`p-3 rounded-lg ${
                      message.sender === "user" ? "bg-blue-600 text-white" : "bg-gray-100 text-gray-900"
                    }`}
                  >
                    <p className="text-sm whitespace-pre-line">{message.text}</p>
                  </div>
                  <p className="text-xs text-gray-500 mt-1">
                    {message.timestamp.toLocaleTimeString("es-PE", {
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </p>
                </div>

                {message.sender === "user" && (
                  <Avatar className="h-8 w-8 bg-gray-100">
                    <AvatarFallback>
                      <User className="h-4 w-4 text-gray-600" />
                    </AvatarFallback>
                  </Avatar>
                )}
              </div>
            ))}

            {isTyping && (
              <div className="flex gap-3 justify-start">
                <Avatar className="h-8 w-8 bg-blue-100">
                  <AvatarFallback>
                    <Bot className="h-4 w-4 text-blue-600" />
                  </AvatarFallback>
                </Avatar>
                <div className="bg-gray-100 p-3 rounded-lg">
                  <div className="flex gap-1">
                    <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce"></div>
                    <div
                      className="w-2 h-2 bg-gray-400 rounded-full animate-bounce"
                      style={{ animationDelay: "0.1s" }}
                    ></div>
                    <div
                      className="w-2 h-2 bg-gray-400 rounded-full animate-bounce"
                      style={{ animationDelay: "0.2s" }}
                    ></div>
                  </div>
                </div>
              </div>
            )}
            <div ref={messagesEndRef} />
          </div>
        </ScrollArea>

        {/* Quick Suggestions */}
        {messages.length <= 1 && (
          <div className="p-4 border-t bg-gray-50">
            <p className="text-sm font-medium text-gray-700 mb-2 flex items-center gap-2">
              <Lightbulb className="h-4 w-4" />
              Preguntas frecuentes:
            </p>
            <div className="space-y-2">
              {quickSuggestions.slice(0, 3).map((suggestion, index) => (
                <Button
                  key={index}
                  variant="outline"
                  size="sm"
                  onClick={() => handleSuggestionClick(suggestion)}
                  className="w-full text-left justify-start h-auto p-2 text-xs"
                >
                  <HelpCircle className="h-3 w-3 mr-2 flex-shrink-0" />
                  {suggestion}
                </Button>
              ))}
            </div>
          </div>
        )}

        {/* Input Area */}
        <div className="p-4 border-t">
          <div className="flex gap-2">
            <Input
              ref={inputRef}
              value={inputMessage}
              onChange={(e) => setInputMessage(e.target.value)}
              onKeyPress={handleKeyPress}
              placeholder="Escribe tu pregunta..."
              className="flex-1"
              disabled={isTyping}
            />
            <Button
              onClick={handleSendMessage}
              disabled={!inputMessage.trim() || isTyping}
              className="bg-blue-600 hover:bg-blue-700"
            >
              <Send className="h-4 w-4" />
            </Button>
          </div>
          <p className="text-xs text-gray-500 mt-2 text-center">
            Presiona Enter para enviar • Shift+Enter para nueva línea
          </p>
        </div>
      </CardContent>
    </Card>
  )
}
