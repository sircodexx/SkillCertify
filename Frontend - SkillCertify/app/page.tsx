"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Badge } from "@/components/ui/badge"
import { Alert, AlertDescription } from "@/components/ui/alert"
import {
  Award,
  BookOpen,
  UsersIcon,
  TrendingUp,
  CheckCircle,
  Star,
  Globe,
  Shield,
  Zap,
  Target,
  ArrowRight,
  Mail,
  Lock,
  Building2,
  Phone,
} from "lucide-react"
import Dashboard from "./dashboard/page"

interface AppUser {
  id: number
  name: string
  email: string
  role: "ADMIN" | "NORMAL"
}

export default function Home() {
  const [currentUser, setCurrentUser] = useState<AppUser | null>(null)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState("")
  const [loginData, setLoginData] = useState({
    email: "",
    password: "",
  })
  const [registerData, setRegisterData] = useState({
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
    phone: "",
    company: "",
  })

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)
    setError("")

    // Simulación de autenticación
    setTimeout(() => {
      if (loginData.email === "admin@skillcertify.pe" && loginData.password === "admin123") {
        setCurrentUser({
          id: 1,
          name: "Administrador",
          email: loginData.email,
          role: "ADMIN",
        })
      } else if (loginData.email === "usuario@skillcertify.pe" && loginData.password === "user123") {
        setCurrentUser({
          id: 2,
          name: "Juan Pérez",
          email: loginData.email,
          role: "NORMAL",
        })
      } else if (loginData.email && loginData.password) {
        setCurrentUser({
          id: 3,
          name: loginData.email.split("@")[0],
          email: loginData.email,
          role: "NORMAL",
        })
      } else {
        setError("Credenciales inválidas. Usa admin@skillcertify.pe/admin123 o usuario@skillcertify.pe/user123")
      }
      setIsLoading(false)
    }, 1000)
  }

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)
    setError("")

    if (registerData.password !== registerData.confirmPassword) {
      setError("Las contraseñas no coinciden")
      setIsLoading(false)
      return
    }

    // Simulación de registro
    setTimeout(() => {
      setCurrentUser({
        id: Date.now(),
        name: registerData.name,
        email: registerData.email,
        role: "NORMAL",
      })
      setIsLoading(false)
    }, 1000)
  }

  const handleLogout = () => {
    setCurrentUser(null)
    setLoginData({ email: "", password: "" })
    setRegisterData({ name: "", email: "", password: "", confirmPassword: "", phone: "", company: "" })
    setError("")
  }

  if (currentUser) {
    return <Dashboard userRole={currentUser.role} currentUser={currentUser} onLogout={handleLogout} />
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
      {/* Header */}
      <header className="bg-white/80 backdrop-blur-sm border-b sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <div className="bg-gradient-to-r from-blue-600 to-purple-600 p-2 rounded-lg mr-3">
                <Award className="h-6 w-6 text-white" />
              </div>
              <div>
                <h1 className="text-xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">
                  SkillCertify
                </h1>
                <p className="text-xs text-gray-500">Plataforma de Certificación Profesional</p>
              </div>
            </div>
            <nav className="hidden md:flex space-x-8">
              <a href="#features" className="text-gray-600 hover:text-blue-600 transition-colors">
                Características
              </a>
              <a href="#stats" className="text-gray-600 hover:text-blue-600 transition-colors">
                Estadísticas
              </a>
              <a href="#testimonials" className="text-gray-600 hover:text-blue-600 transition-colors">
                Testimonios
              </a>
              <a href="#contact" className="text-gray-600 hover:text-blue-600 transition-colors">
                Contacto
              </a>
            </nav>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
          {/* Hero Section */}
          <div className="space-y-8">
            <div className="space-y-4">
              <Badge className="bg-blue-100 text-blue-800 border-blue-200" variant="outline">
                🚀 Plataforma Líder en Certificación Tecnológica
              </Badge>
              <h1 className="text-4xl lg:text-5xl font-bold text-gray-900 leading-tight">
                Certifica tus{" "}
                <span className="bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">
                  Habilidades Digitales
                </span>{" "}
                con SkillCertify
              </h1>
              <p className="text-xl text-gray-600 leading-relaxed">
                La plataforma más completa de Perú para obtener certificaciones profesionales en tecnología. Evalúa tus
                conocimientos, obtén certificados reconocidos y acelera tu carrera profesional.
              </p>
            </div>

            {/* Features Grid */}
            <div className="grid grid-cols-2 gap-4">
              <div className="flex items-center space-x-3 p-4 bg-white rounded-lg shadow-sm border">
                <div className="bg-green-100 p-2 rounded-full">
                  <CheckCircle className="h-5 w-5 text-green-600" />
                </div>
                <div>
                  <p className="font-medium text-gray-900">Certificados Oficiales</p>
                  <p className="text-sm text-gray-500">Reconocidos por empresas</p>
                </div>
              </div>
              <div className="flex items-center space-x-3 p-4 bg-white rounded-lg shadow-sm border">
                <div className="bg-blue-100 p-2 rounded-full">
                  <Zap className="h-5 w-5 text-blue-600" />
                </div>
                <div>
                  <p className="font-medium text-gray-900">Evaluación Rápida</p>
                  <p className="text-sm text-gray-500">Resultados inmediatos</p>
                </div>
              </div>
              <div className="flex items-center space-x-3 p-4 bg-white rounded-lg shadow-sm border">
                <div className="bg-purple-100 p-2 rounded-full">
                  <Target className="h-5 w-5 text-purple-600" />
                </div>
                <div>
                  <p className="font-medium text-gray-900">Ruta Personalizada</p>
                  <p className="text-sm text-gray-500">Aprendizaje progresivo</p>
                </div>
              </div>
              <div className="flex items-center space-x-3 p-4 bg-white rounded-lg shadow-sm border">
                <div className="bg-orange-100 p-2 rounded-full">
                  <Shield className="h-5 w-5 text-orange-600" />
                </div>
                <div>
                  <p className="font-medium text-gray-900">100% Seguro</p>
                  <p className="text-sm text-gray-500">Certificados verificables</p>
                </div>
              </div>
            </div>

            {/* CTA Buttons */}
            <div className="flex flex-col sm:flex-row gap-4">
              <Button
                size="lg"
                className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700"
              >
                <Award className="h-5 w-5 mr-2" />
                Comenzar Certificación
                <ArrowRight className="h-5 w-5 ml-2" />
              </Button>
              <Button variant="outline" size="lg">
                <BookOpen className="h-5 w-5 mr-2" />
                Ver Evaluaciones Disponibles
              </Button>
            </div>
          </div>

          {/* Auth Section */}
          <div className="lg:pl-8">
            <Card className="shadow-xl border-0 bg-white/80 backdrop-blur-sm">
              <CardHeader className="text-center pb-6">
                <CardTitle className="text-2xl">Accede a tu Cuenta</CardTitle>
                <CardDescription>Inicia sesión o regístrate para comenzar tu certificación</CardDescription>
              </CardHeader>
              <CardContent>
                {error && (
                  <Alert className="mb-6 bg-red-50 border-red-200">
                    <AlertDescription className="text-red-800">{error}</AlertDescription>
                  </Alert>
                )}

                <Tabs defaultValue="login" className="space-y-6">
                  <TabsList className="grid w-full grid-cols-2">
                    <TabsTrigger value="login">Iniciar Sesión</TabsTrigger>
                    <TabsTrigger value="register">Registrarse</TabsTrigger>
                  </TabsList>

                  <TabsContent value="login">
                    <form onSubmit={handleLogin} className="space-y-4">
                      <div className="space-y-2">
                        <Label htmlFor="email">Email</Label>
                        <div className="relative">
                          <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                          <Input
                            id="email"
                            type="email"
                            placeholder="tu@email.com"
                            value={loginData.email}
                            onChange={(e) => setLoginData({ ...loginData, email: e.target.value })}
                            className="pl-10"
                            required
                          />
                        </div>
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor="password">Contraseña</Label>
                        <div className="relative">
                          <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                          <Input
                            id="password"
                            type="password"
                            placeholder="••••••••"
                            value={loginData.password}
                            onChange={(e) => setLoginData({ ...loginData, password: e.target.value })}
                            className="pl-10"
                            required
                          />
                        </div>
                      </div>
                      <Button type="submit" className="w-full" disabled={isLoading}>
                        {isLoading ? "Iniciando sesión..." : "Iniciar Sesión"}
                      </Button>
                    </form>

                    <div className="mt-6 p-4 bg-blue-50 rounded-lg">
                      <p className="text-sm font-medium text-blue-800 mb-2">Cuentas de prueba:</p>
                      <div className="space-y-1 text-xs text-blue-700">
                        <p>
                          <strong>Admin:</strong> admin@skillcertify.pe / admin123
                        </p>
                        <p>
                          <strong>Usuario:</strong> usuario@skillcertify.pe / user123
                        </p>
                      </div>
                    </div>
                  </TabsContent>

                  <TabsContent value="register">
                    <form onSubmit={handleRegister} className="space-y-4">
                      <div className="space-y-2">
                        <Label htmlFor="name">Nombre Completo</Label>
                        <div className="relative">
                          <Building2 className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                          <Input
                            id="name"
                            placeholder="Tu nombre completo"
                            value={registerData.name}
                            onChange={(e) => setRegisterData({ ...registerData, name: e.target.value })}
                            className="pl-10"
                            required
                          />
                        </div>
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor="register-email">Email</Label>
                        <div className="relative">
                          <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                          <Input
                            id="register-email"
                            type="email"
                            placeholder="tu@email.com"
                            value={registerData.email}
                            onChange={(e) => setRegisterData({ ...registerData, email: e.target.value })}
                            className="pl-10"
                            required
                          />
                        </div>
                      </div>
                      <div className="grid grid-cols-2 gap-4">
                        <div className="space-y-2">
                          <Label htmlFor="phone">Teléfono</Label>
                          <div className="relative">
                            <Phone className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                            <Input
                              id="phone"
                              placeholder="+51 999 888 777"
                              value={registerData.phone}
                              onChange={(e) => setRegisterData({ ...registerData, phone: e.target.value })}
                              className="pl-10"
                            />
                          </div>
                        </div>
                        <div className="space-y-2">
                          <Label htmlFor="company">Empresa (Opcional)</Label>
                          <div className="relative">
                            <Building2 className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                            <Input
                              id="company"
                              placeholder="Tu empresa"
                              value={registerData.company}
                              onChange={(e) => setRegisterData({ ...registerData, company: e.target.value })}
                              className="pl-10"
                            />
                          </div>
                        </div>
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor="register-password">Contraseña</Label>
                        <div className="relative">
                          <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                          <Input
                            id="register-password"
                            type="password"
                            placeholder="••••••••"
                            value={registerData.password}
                            onChange={(e) => setRegisterData({ ...registerData, password: e.target.value })}
                            className="pl-10"
                            required
                          />
                        </div>
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor="confirm-password">Confirmar Contraseña</Label>
                        <div className="relative">
                          <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                          <Input
                            id="confirm-password"
                            type="password"
                            placeholder="••••••••"
                            value={registerData.confirmPassword}
                            onChange={(e) => setRegisterData({ ...registerData, confirmPassword: e.target.value })}
                            className="pl-10"
                            required
                          />
                        </div>
                      </div>
                      <Button type="submit" className="w-full" disabled={isLoading}>
                        {isLoading ? "Creando cuenta..." : "Crear Cuenta"}
                      </Button>
                    </form>
                  </TabsContent>
                </Tabs>
              </CardContent>
            </Card>
          </div>
        </div>

        {/* Stats Section */}
        <section id="stats" className="mt-24">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-gray-900 mb-4">Números que Hablan por Nosotros</h2>
            <p className="text-xl text-gray-600">Más de 10,000 profesionales han confiado en SkillCertify</p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div className="text-center p-6 bg-white rounded-xl shadow-sm border">
              <div className="bg-blue-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <UsersIcon className="h-8 w-8 text-blue-600" />
              </div>
              <div className="text-3xl font-bold text-gray-900 mb-2">10,000+</div>
              <div className="text-gray-600">Profesionales Certificados</div>
            </div>
            <div className="text-center p-6 bg-white rounded-xl shadow-sm border">
              <div className="bg-green-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <Award className="h-8 w-8 text-green-600" />
              </div>
              <div className="text-3xl font-bold text-gray-900 mb-2">50+</div>
              <div className="text-gray-600">Certificaciones Disponibles</div>
            </div>
            <div className="text-center p-6 bg-white rounded-xl shadow-sm border">
              <div className="bg-purple-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <Building2 className="h-8 w-8 text-purple-600" />
              </div>
              <div className="text-3xl font-bold text-gray-900 mb-2">500+</div>
              <div className="text-gray-600">Empresas Aliadas</div>
            </div>
            <div className="text-center p-6 bg-white rounded-xl shadow-sm border">
              <div className="bg-orange-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <TrendingUp className="h-8 w-8 text-orange-600" />
              </div>
              <div className="text-3xl font-bold text-gray-900 mb-2">95%</div>
              <div className="text-gray-600">Tasa de Satisfacción</div>
            </div>
          </div>
        </section>

        {/* Features Section */}
        <section id="features" className="mt-24">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-gray-900 mb-4">¿Por qué elegir SkillCertify?</h2>
            <p className="text-xl text-gray-600">La plataforma más completa para tu desarrollo profesional</p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <Card className="border-0 shadow-lg hover:shadow-xl transition-shadow">
              <CardHeader>
                <div className="bg-blue-100 w-12 h-12 rounded-lg flex items-center justify-center mb-4">
                  <Globe className="h-6 w-6 text-blue-600" />
                </div>
                <CardTitle>Reconocimiento Internacional</CardTitle>
                <CardDescription>
                  Nuestros certificados son reconocidos por empresas líderes en Perú y Latinoamérica
                </CardDescription>
              </CardHeader>
            </Card>
            <Card className="border-0 shadow-lg hover:shadow-xl transition-shadow">
              <CardHeader>
                <div className="bg-green-100 w-12 h-12 rounded-lg flex items-center justify-center mb-4">
                  <Zap className="h-6 w-6 text-green-600" />
                </div>
                <CardTitle>Evaluación Inteligente</CardTitle>
                <CardDescription>
                  Sistema adaptativo que se ajusta a tu nivel y te proporciona feedback personalizado
                </CardDescription>
              </CardHeader>
            </Card>
            <Card className="border-0 shadow-lg hover:shadow-xl transition-shadow">
              <CardHeader>
                <div className="bg-purple-100 w-12 h-12 rounded-lg flex items-center justify-center mb-4">
                  <Target className="h-6 w-6 text-purple-600" />
                </div>
                <CardTitle>Ruta de Aprendizaje</CardTitle>
                <CardDescription>
                  Progresión estructurada con prerrequisitos que garantizan un aprendizaje sólido
                </CardDescription>
              </CardHeader>
            </Card>
          </div>
        </section>

        {/* Testimonials Section */}
        <section id="testimonials" className="mt-24">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-gray-900 mb-4">Lo que dicen nuestros usuarios</h2>
            <p className="text-xl text-gray-600">Historias de éxito de profesionales como tú</p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <Card className="border-0 shadow-lg">
              <CardContent className="p-6">
                <div className="flex items-center mb-4">
                  {[...Array(5)].map((_, i) => (
                    <Star key={i} className="h-4 w-4 text-yellow-400 fill-current" />
                  ))}
                </div>
                <p className="text-gray-700 mb-4">
                  "SkillCertify me ayudó a conseguir mi trabajo actual. Los certificados son muy valorados por las
                  empresas."
                </p>
                <div className="flex items-center">
                  <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center mr-3">
                    <span className="text-blue-600 font-medium">MP</span>
                  </div>
                  <div>
                    <p className="font-medium">María Pérez</p>
                    <p className="text-sm text-gray-500">Desarrolladora Frontend</p>
                  </div>
                </div>
              </CardContent>
            </Card>
            <Card className="border-0 shadow-lg">
              <CardContent className="p-6">
                <div className="flex items-center mb-4">
                  {[...Array(5)].map((_, i) => (
                    <Star key={i} className="h-4 w-4 text-yellow-400 fill-current" />
                  ))}
                </div>
                <p className="text-gray-700 mb-4">
                  "La plataforma es muy intuitiva y las evaluaciones están muy bien diseñadas. Recomendado 100%."
                </p>
                <div className="flex items-center">
                  <div className="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center mr-3">
                    <span className="text-green-600 font-medium">CG</span>
                  </div>
                  <div>
                    <p className="font-medium">Carlos García</p>
                    <p className="text-sm text-gray-500">Analista de Datos</p>
                  </div>
                </div>
              </CardContent>
            </Card>
            <Card className="border-0 shadow-lg">
              <CardContent className="p-6">
                <div className="flex items-center mb-4">
                  {[...Array(5)].map((_, i) => (
                    <Star key={i} className="h-4 w-4 text-yellow-400 fill-current" />
                  ))}
                </div>
                <p className="text-gray-700 mb-4">
                  "Obtuve 3 certificaciones en 2 meses. El sistema de prerrequisitos me ayudó a aprender de forma
                  ordenada."
                </p>
                <div className="flex items-center">
                  <div className="w-10 h-10 bg-purple-100 rounded-full flex items-center justify-center mr-3">
                    <span className="text-purple-600 font-medium">AL</span>
                  </div>
                  <div>
                    <p className="font-medium">Ana López</p>
                    <p className="text-sm text-gray-500">UX Designer</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </section>
      </div>

      {/* Footer */}
      <footer className="bg-gray-900 text-white mt-24">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div>
              <div className="flex items-center mb-4">
                <div className="bg-gradient-to-r from-blue-600 to-purple-600 p-2 rounded-lg mr-3">
                  <Award className="h-6 w-6 text-white" />
                </div>
                <h3 className="text-xl font-bold">SkillCertify</h3>
              </div>
              <p className="text-gray-400">
                La plataforma líder en certificación profesional de Perú. Acelera tu carrera con nuestros certificados
                reconocidos.
              </p>
            </div>
            <div>
              <h4 className="font-semibold mb-4">Certificaciones</h4>
              <ul className="space-y-2 text-gray-400">
                <li>Desarrollo Web</li>
                <li>Análisis de Datos</li>
                <li>Diseño UX/UI</li>
                <li>Marketing Digital</li>
              </ul>
            </div>
            <div>
              <h4 className="font-semibold mb-4">Empresa</h4>
              <ul className="space-y-2 text-gray-400">
                <li>Sobre Nosotros</li>
                <li>Empresas Aliadas</li>
                <li>Testimonios</li>
                <li>Blog</li>
              </ul>
            </div>
            <div id="contact">
              <h4 className="font-semibold mb-4">Contacto</h4>
              <ul className="space-y-2 text-gray-400">
                <li>soporte@skillcertify.pe</li>
                <li>+51 999 888 777</li>
                <li>Lima, Perú</li>
                <li>Lun - Vie: 8:00 AM - 6:00 PM</li>
              </ul>
            </div>
          </div>
          <div className="border-t border-gray-800 mt-8 pt-8 text-center text-gray-400">
            <p>&copy; 2024 SkillCertify. Todos los derechos reservados.</p>
          </div>
        </div>
      </footer>
    </div>
  )
}
