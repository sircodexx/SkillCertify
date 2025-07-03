"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Badge } from "@/components/ui/badge"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import {
  Users,
  Plus,
  Edit,
  Trash2,
  Mail,
  Phone,
  CheckCircle,
  Award,
  BookOpen,
  Search,
  Filter,
  Download,
  Upload,
} from "lucide-react"

interface Student {
  id: number
  name: string
  email: string
  phone: string
  dni: string
  birthDate: string
  centerId: number
  centerName: string
  enrollmentDate: string
  status: "Activo" | "Inactivo" | "Graduado"
  completedEvaluations: number
  certificates: number
  averageScore: number
}

interface Center {
  id: number
  name: string
}

export default function StudentsManagement() {
  const [students, setStudents] = useState<Student[]>([
    {
      id: 1,
      name: "Juan Carlos Pérez",
      email: "juan.perez@email.com",
      phone: "+51 987 654 321",
      dni: "12345678",
      birthDate: "1995-03-15",
      centerId: 1,
      centerName: "Centro Lima Norte",
      enrollmentDate: "2024-01-15",
      status: "Activo",
      completedEvaluations: 5,
      certificates: 3,
      averageScore: 85,
    },
    {
      id: 2,
      name: "María González López",
      email: "maria.gonzalez@email.com",
      phone: "+51 987 123 456",
      dni: "87654321",
      birthDate: "1992-07-22",
      centerId: 1,
      centerName: "Centro Lima Norte",
      enrollmentDate: "2024-01-10",
      status: "Activo",
      completedEvaluations: 8,
      certificates: 6,
      averageScore: 92,
    },
    {
      id: 3,
      name: "Carlos Mendoza Quispe",
      email: "carlos.mendoza@email.com",
      phone: "+51 954 789 123",
      dni: "11223344",
      birthDate: "1988-11-08",
      centerId: 2,
      centerName: "Centro Arequipa",
      enrollmentDate: "2024-01-05",
      status: "Graduado",
      completedEvaluations: 12,
      certificates: 10,
      averageScore: 88,
    },
  ])

  const [centers] = useState<Center[]>([
    { id: 1, name: "Centro Lima Norte" },
    { id: 2, name: "Centro Arequipa" },
    { id: 3, name: "Centro Cusco" },
  ])

  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [editingStudent, setEditingStudent] = useState<Student | null>(null)
  const [searchTerm, setSearchTerm] = useState("")
  const [filterStatus, setFilterStatus] = useState<string>("all")
  const [filterCenter, setFilterCenter] = useState<string>("all")
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    dni: "",
    birthDate: "",
    centerId: 0,
    status: "Activo" as "Activo" | "Inactivo" | "Graduado",
  })
  const [showSuccess, setShowSuccess] = useState(false)

  const handleCreateStudent = () => {
    setEditingStudent(null)
    setFormData({
      name: "",
      email: "",
      phone: "",
      dni: "",
      birthDate: "",
      centerId: 0,
      status: "Activo",
    })
    setIsDialogOpen(true)
  }

  const handleEditStudent = (student: Student) => {
    setEditingStudent(student)
    setFormData({
      name: student.name,
      email: student.email,
      phone: student.phone,
      dni: student.dni,
      birthDate: student.birthDate,
      centerId: student.centerId,
      status: student.status,
    })
    setIsDialogOpen(true)
  }

  const handleSaveStudent = () => {
    const centerName = centers.find((c) => c.id === formData.centerId)?.name || ""

    if (editingStudent) {
      // Actualizar estudiante existente
      setStudents((prev) =>
        prev.map((student) =>
          student.id === editingStudent.id
            ? {
                ...student,
                ...formData,
                centerName,
              }
            : student,
        ),
      )
    } else {
      // Crear nuevo estudiante
      const newStudent: Student = {
        id: Date.now(),
        ...formData,
        centerName,
        enrollmentDate: new Date().toISOString(),
        completedEvaluations: 0,
        certificates: 0,
        averageScore: 0,
      }
      setStudents((prev) => [newStudent, ...prev])
    }

    setIsDialogOpen(false)
    setShowSuccess(true)
    setTimeout(() => setShowSuccess(false), 3000)
  }

  const handleDeleteStudent = (id: number) => {
    if (confirm("¿Estás seguro de que deseas eliminar este estudiante?")) {
      setStudents((prev) => prev.filter((student) => student.id !== id))
    }
  }

  const filteredStudents = students.filter((student) => {
    const matchesSearch =
      student.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.dni.includes(searchTerm)
    const matchesStatus = filterStatus === "all" || student.status === filterStatus
    const matchesCenter = filterCenter === "all" || student.centerId.toString() === filterCenter

    return matchesSearch && matchesStatus && matchesCenter
  })

  const getStatusColor = (status: string) => {
    switch (status) {
      case "Activo":
        return "bg-green-100 text-green-800 border-green-200"
      case "Inactivo":
        return "bg-red-100 text-red-800 border-red-200"
      case "Graduado":
        return "bg-blue-100 text-blue-800 border-blue-200"
      default:
        return "bg-gray-100 text-gray-800 border-gray-200"
    }
  }

  const getScoreColor = (score: number) => {
    if (score >= 90) return "text-green-600"
    if (score >= 70) return "text-yellow-600"
    return "text-red-600"
  }

  return (
    <div className="space-y-6">
      {showSuccess && (
        <Alert className="bg-green-50 border-green-200">
          <CheckCircle className="h-4 w-4" />
          <AlertDescription className="text-green-800">¡Estudiante guardado exitosamente!</AlertDescription>
        </Alert>
      )}

      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Gestión de Estudiantes</h2>
          <p className="text-gray-600 mt-1">Administra los estudiantes registrados en la plataforma</p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline">
            <Upload className="h-4 w-4 mr-2" />
            Importar
          </Button>
          <Button variant="outline">
            <Download className="h-4 w-4 mr-2" />
            Exportar
          </Button>
          <Button onClick={handleCreateStudent}>
            <Plus className="h-4 w-4 mr-2" />
            Nuevo Estudiante
          </Button>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Estudiantes</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{students.length}</div>
            <p className="text-xs text-muted-foreground">
              {students.filter((s) => s.status === "Activo").length} activos
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Graduados</CardTitle>
            <Award className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{students.filter((s) => s.status === "Graduado").length}</div>
            <p className="text-xs text-muted-foreground">
              {Math.round((students.filter((s) => s.status === "Graduado").length / students.length) * 100)}% del total
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Evaluaciones Completadas</CardTitle>
            <BookOpen className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{students.reduce((acc, s) => acc + s.completedEvaluations, 0)}</div>
            <p className="text-xs text-muted-foreground">Total en la plataforma</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Promedio General</CardTitle>
            <CheckCircle className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {Math.round(students.reduce((acc, s) => acc + s.averageScore, 0) / students.length)}%
            </div>
            <p className="text-xs text-muted-foreground">Puntuación promedio</p>
          </CardContent>
        </Card>
      </div>

      {/* Filters */}
      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Filtros y Búsqueda</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="space-y-2">
              <Label htmlFor="search">Buscar</Label>
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                <Input
                  id="search"
                  placeholder="Nombre, email o DNI..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="status-filter">Estado</Label>
              <Select value={filterStatus} onValueChange={setFilterStatus}>
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">Todos los estados</SelectItem>
                  <SelectItem value="Activo">Activo</SelectItem>
                  <SelectItem value="Inactivo">Inactivo</SelectItem>
                  <SelectItem value="Graduado">Graduado</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label htmlFor="center-filter">Centro</Label>
              <Select value={filterCenter} onValueChange={setFilterCenter}>
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">Todos los centros</SelectItem>
                  {centers.map((center) => (
                    <SelectItem key={center.id} value={center.id.toString()}>
                      {center.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div className="flex items-end">
              <Button
                variant="outline"
                onClick={() => {
                  setSearchTerm("")
                  setFilterStatus("all")
                  setFilterCenter("all")
                }}
                className="w-full"
              >
                <Filter className="h-4 w-4 mr-2" />
                Limpiar Filtros
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Students Table */}
      <Card>
        <CardHeader>
          <CardTitle>Estudiantes ({filteredStudents.length})</CardTitle>
          <CardDescription>Lista de estudiantes registrados</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {filteredStudents.map((student) => (
              <div
                key={student.id}
                className="flex items-center justify-between p-4 border rounded-lg hover:bg-gray-50 transition-colors"
              >
                <div className="flex-1 grid grid-cols-1 md:grid-cols-6 gap-4">
                  <div className="md:col-span-2">
                    <h4 className="font-medium text-gray-900">{student.name}</h4>
                    <div className="flex items-center text-sm text-gray-500 mt-1">
                      <Mail className="h-3 w-3 mr-1" />
                      {student.email}
                    </div>
                    <div className="flex items-center text-sm text-gray-500">
                      <Phone className="h-3 w-3 mr-1" />
                      {student.phone}
                    </div>
                  </div>

                  <div>
                    <p className="text-sm font-medium text-gray-900">DNI</p>
                    <p className="text-sm text-gray-500">{student.dni}</p>
                    <p className="text-xs text-gray-400 mt-1">{student.centerName}</p>
                  </div>

                  <div>
                    <p className="text-sm font-medium text-gray-900">Estado</p>
                    <Badge className={getStatusColor(student.status)} variant="outline">
                      {student.status}
                    </Badge>
                    <p className="text-xs text-gray-400 mt-1">
                      Desde: {new Date(student.enrollmentDate).toLocaleDateString("es-PE")}
                    </p>
                  </div>

                  <div>
                    <p className="text-sm font-medium text-gray-900">Progreso</p>
                    <p className="text-sm text-gray-500">{student.completedEvaluations} evaluaciones</p>
                    <p className="text-sm text-gray-500">{student.certificates} certificados</p>
                  </div>

                  <div>
                    <p className="text-sm font-medium text-gray-900">Promedio</p>
                    <p className={`text-lg font-bold ${getScoreColor(student.averageScore)}`}>
                      {student.averageScore}%
                    </p>
                  </div>
                </div>

                <div className="flex space-x-1 ml-4">
                  <Button variant="ghost" size="sm" onClick={() => handleEditStudent(student)} className="h-8 w-8 p-0">
                    <Edit className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => handleDeleteStudent(student.id)}
                    className="h-8 w-8 p-0 text-red-600 hover:text-red-700"
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            ))}

            {filteredStudents.length === 0 && (
              <div className="text-center py-8">
                <Users className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                <h3 className="text-lg font-medium text-gray-900 mb-2">No se encontraron estudiantes</h3>
                <p className="text-gray-600">
                  {searchTerm || filterStatus !== "all" || filterCenter !== "all"
                    ? "Intenta ajustar los filtros de búsqueda"
                    : "Crea tu primer estudiante para comenzar"}
                </p>
              </div>
            )}
          </div>
        </CardContent>
      </Card>

      {/* Create/Edit Dialog */}
      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>{editingStudent ? "Editar Estudiante" : "Crear Nuevo Estudiante"}</DialogTitle>
            <DialogDescription>
              {editingStudent
                ? "Modifica la información del estudiante"
                : "Completa la información para registrar un nuevo estudiante"}
            </DialogDescription>
          </DialogHeader>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="name">Nombre Completo</Label>
              <Input
                id="name"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                placeholder="Nombre completo del estudiante"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="dni">DNI</Label>
              <Input
                id="dni"
                value={formData.dni}
                onChange={(e) => setFormData({ ...formData, dni: e.target.value })}
                placeholder="12345678"
                maxLength={8}
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                placeholder="estudiante@email.com"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="phone">Teléfono</Label>
              <Input
                id="phone"
                value={formData.phone}
                onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                placeholder="+51 987 654 321"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="birthDate">Fecha de Nacimiento</Label>
              <Input
                id="birthDate"
                type="date"
                value={formData.birthDate}
                onChange={(e) => setFormData({ ...formData, birthDate: e.target.value })}
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="center">Centro de Capacitación</Label>
              <Select
                value={formData.centerId.toString()}
                onValueChange={(value) => setFormData({ ...formData, centerId: Number.parseInt(value) })}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Selecciona un centro" />
                </SelectTrigger>
                <SelectContent>
                  {centers.map((center) => (
                    <SelectItem key={center.id} value={center.id.toString()}>
                      {center.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label htmlFor="status">Estado</Label>
              <Select
                value={formData.status}
                onValueChange={(value: "Activo" | "Inactivo" | "Graduado") =>
                  setFormData({ ...formData, status: value })
                }
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="Activo">Activo</SelectItem>
                  <SelectItem value="Inactivo">Inactivo</SelectItem>
                  <SelectItem value="Graduado">Graduado</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>

          <div className="flex justify-end space-x-2 pt-4">
            <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
              Cancelar
            </Button>
            <Button onClick={handleSaveStudent}>{editingStudent ? "Actualizar" : "Crear"} Estudiante</Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  )
}
