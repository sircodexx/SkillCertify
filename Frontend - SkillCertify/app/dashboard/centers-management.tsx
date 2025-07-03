"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Badge } from "@/components/ui/badge"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import {
  Building2,
  Plus,
  Edit,
  Trash2,
  MapPin,
  Phone,
  Mail,
  Users,
  Calendar,
  CheckCircle,
  AlertCircle,
} from "lucide-react"

interface Center {
  id: number
  name: string
  description: string
  address: string
  city: string
  region: string
  phone: string
  email: string
  director: string
  capacity: number
  studentsCount: number
  status: "Activo" | "Inactivo"
  createdAt: string
  updatedAt: string
}

export default function CentersManagement() {
  const [centers, setCenters] = useState<Center[]>([
    {
      id: 1,
      name: "Centro de Capacitación Lima Norte",
      description: "Centro especializado en tecnologías de la información y desarrollo web",
      address: "Av. Túpac Amaru 1234",
      city: "Lima",
      region: "Lima",
      phone: "+51 1 234-5678",
      email: "lima.norte@skillcertify.pe",
      director: "María González",
      capacity: 200,
      studentsCount: 156,
      status: "Activo",
      createdAt: "2024-01-15",
      updatedAt: "2024-01-20",
    },
    {
      id: 2,
      name: "Centro de Capacitación Arequipa",
      description: "Centro enfocado en diseño gráfico y marketing digital",
      address: "Calle Mercaderes 456",
      city: "Arequipa",
      region: "Arequipa",
      phone: "+51 54 987-6543",
      email: "arequipa@skillcertify.pe",
      director: "Carlos Mendoza",
      capacity: 150,
      studentsCount: 89,
      status: "Activo",
      createdAt: "2024-01-10",
      updatedAt: "2024-01-18",
    },
    {
      id: 3,
      name: "Centro de Capacitación Cusco",
      description: "Centro de formación en análisis de datos y business intelligence",
      address: "Plaza de Armas 789",
      city: "Cusco",
      region: "Cusco",
      phone: "+51 84 456-7890",
      email: "cusco@skillcertify.pe",
      director: "Ana Quispe",
      capacity: 100,
      studentsCount: 67,
      status: "Activo",
      createdAt: "2024-01-05",
      updatedAt: "2024-01-15",
    },
  ])

  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [editingCenter, setEditingCenter] = useState<Center | null>(null)
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    address: "",
    city: "",
    region: "",
    phone: "",
    email: "",
    director: "",
    capacity: 100,
    status: "Activo" as "Activo" | "Inactivo",
  })
  const [showSuccess, setShowSuccess] = useState(false)

  const regions = [
    "Lima",
    "Arequipa",
    "Cusco",
    "La Libertad",
    "Piura",
    "Lambayeque",
    "Junín",
    "Ica",
    "Ancash",
    "Huánuco",
    "Cajamarca",
    "Puno",
    "Loreto",
    "Ucayali",
    "San Martín",
    "Tacna",
    "Tumbes",
    "Amazonas",
    "Apurímac",
    "Ayacucho",
    "Huancavelica",
    "Madre de Dios",
    "Moquegua",
    "Pasco",
  ]

  const handleCreateCenter = () => {
    setEditingCenter(null)
    setFormData({
      name: "",
      description: "",
      address: "",
      city: "",
      region: "",
      phone: "",
      email: "",
      director: "",
      capacity: 100,
      status: "Activo",
    })
    setIsDialogOpen(true)
  }

  const handleEditCenter = (center: Center) => {
    setEditingCenter(center)
    setFormData({
      name: center.name,
      description: center.description,
      address: center.address,
      city: center.city,
      region: center.region,
      phone: center.phone,
      email: center.email,
      director: center.director,
      capacity: center.capacity,
      status: center.status,
    })
    setIsDialogOpen(true)
  }

  const handleSaveCenter = () => {
    if (editingCenter) {
      // Actualizar centro existente
      setCenters((prev) =>
        prev.map((center) =>
          center.id === editingCenter.id ? { ...center, ...formData, updatedAt: new Date().toISOString() } : center,
        ),
      )
    } else {
      // Crear nuevo centro
      const newCenter: Center = {
        id: Date.now(),
        ...formData,
        studentsCount: 0,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      }
      setCenters((prev) => [newCenter, ...prev])
    }

    setIsDialogOpen(false)
    setShowSuccess(true)
    setTimeout(() => setShowSuccess(false), 3000)
  }

  const handleDeleteCenter = (id: number) => {
    if (confirm("¿Estás seguro de que deseas eliminar este centro?")) {
      setCenters((prev) => prev.filter((center) => center.id !== id))
    }
  }

  const getStatusColor = (status: string) => {
    return status === "Activo"
      ? "bg-green-100 text-green-800 border-green-200"
      : "bg-red-100 text-red-800 border-red-200"
  }

  const getCapacityColor = (current: number, total: number) => {
    const percentage = (current / total) * 100
    if (percentage >= 90) return "text-red-600"
    if (percentage >= 70) return "text-yellow-600"
    return "text-green-600"
  }

  return (
    <div className="space-y-6">
      {showSuccess && (
        <Alert className="bg-green-50 border-green-200">
          <CheckCircle className="h-4 w-4" />
          <AlertDescription className="text-green-800">¡Centro guardado exitosamente!</AlertDescription>
        </Alert>
      )}

      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Gestión de Centros</h2>
          <p className="text-gray-600 mt-1">Administra los centros de capacitación</p>
        </div>
        <Button onClick={handleCreateCenter}>
          <Plus className="h-4 w-4 mr-2" />
          Nuevo Centro
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Centros</CardTitle>
            <Building2 className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{centers.length}</div>
            <p className="text-xs text-muted-foreground">
              {centers.filter((c) => c.status === "Activo").length} activos
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Estudiantes</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{centers.reduce((acc, center) => acc + center.studentsCount, 0)}</div>
            <p className="text-xs text-muted-foreground">En todos los centros</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Capacidad Total</CardTitle>
            <Calendar className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{centers.reduce((acc, center) => acc + center.capacity, 0)}</div>
            <p className="text-xs text-muted-foreground">Estudiantes máximo</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Ocupación Promedio</CardTitle>
            <AlertCircle className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {Math.round(
                (centers.reduce((acc, center) => acc + center.studentsCount, 0) /
                  centers.reduce((acc, center) => acc + center.capacity, 0)) *
                  100,
              )}
              %
            </div>
            <p className="text-xs text-muted-foreground">De capacidad utilizada</p>
          </CardContent>
        </Card>
      </div>

      {/* Centers Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
        {centers.map((center) => (
          <Card key={center.id} className="hover:shadow-lg transition-shadow">
            <CardHeader>
              <div className="flex justify-between items-start">
                <div className="flex-1">
                  <CardTitle className="text-lg mb-2">{center.name}</CardTitle>
                  <CardDescription className="line-clamp-2">{center.description}</CardDescription>
                </div>
                <div className="flex space-x-1 ml-2">
                  <Button variant="ghost" size="sm" onClick={() => handleEditCenter(center)} className="h-8 w-8 p-0">
                    <Edit className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => handleDeleteCenter(center.id)}
                    className="h-8 w-8 p-0 text-red-600 hover:text-red-700"
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </div>
              <Badge className={getStatusColor(center.status)} variant="outline">
                {center.status}
              </Badge>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <div className="flex items-center text-sm text-gray-600">
                  <MapPin className="h-4 w-4 mr-2" />
                  {center.address}, {center.city}
                </div>
                <div className="flex items-center text-sm text-gray-600">
                  <Phone className="h-4 w-4 mr-2" />
                  {center.phone}
                </div>
                <div className="flex items-center text-sm text-gray-600">
                  <Mail className="h-4 w-4 mr-2" />
                  {center.email}
                </div>
                <div className="flex items-center text-sm text-gray-600">
                  <Users className="h-4 w-4 mr-2" />
                  Director: {center.director}
                </div>
              </div>

              <div className="pt-2 border-t">
                <div className="flex justify-between items-center mb-2">
                  <span className="text-sm font-medium">Ocupación</span>
                  <span className={`text-sm font-bold ${getCapacityColor(center.studentsCount, center.capacity)}`}>
                    {center.studentsCount}/{center.capacity}
                  </span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div
                    className="bg-blue-600 h-2 rounded-full transition-all duration-300"
                    style={{ width: `${(center.studentsCount / center.capacity) * 100}%` }}
                  ></div>
                </div>
                <div className="text-xs text-gray-500 mt-1">
                  {Math.round((center.studentsCount / center.capacity) * 100)}% de capacidad
                </div>
              </div>

              <div className="text-xs text-gray-500">
                Creado: {new Date(center.createdAt).toLocaleDateString("es-PE")}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Create/Edit Dialog */}
      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>{editingCenter ? "Editar Centro" : "Crear Nuevo Centro"}</DialogTitle>
            <DialogDescription>
              {editingCenter
                ? "Modifica la información del centro de capacitación"
                : "Completa la información para crear un nuevo centro de capacitación"}
            </DialogDescription>
          </DialogHeader>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="name">Nombre del Centro</Label>
              <Input
                id="name"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                placeholder="Ej: Centro Lima Norte"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="director">Director</Label>
              <Input
                id="director"
                value={formData.director}
                onChange={(e) => setFormData({ ...formData, director: e.target.value })}
                placeholder="Nombre del director"
              />
            </div>

            <div className="md:col-span-2 space-y-2">
              <Label htmlFor="description">Descripción</Label>
              <Textarea
                id="description"
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                placeholder="Describe las especialidades y características del centro"
                rows={3}
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="address">Dirección</Label>
              <Input
                id="address"
                value={formData.address}
                onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                placeholder="Dirección completa"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="city">Ciudad</Label>
              <Input
                id="city"
                value={formData.city}
                onChange={(e) => setFormData({ ...formData, city: e.target.value })}
                placeholder="Ciudad"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="region">Región</Label>
              <Select value={formData.region} onValueChange={(value) => setFormData({ ...formData, region: value })}>
                <SelectTrigger>
                  <SelectValue placeholder="Selecciona una región" />
                </SelectTrigger>
                <SelectContent>
                  {regions.map((region) => (
                    <SelectItem key={region} value={region}>
                      {region}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label htmlFor="phone">Teléfono</Label>
              <Input
                id="phone"
                value={formData.phone}
                onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                placeholder="+51 1 234-5678"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                placeholder="centro@skillcertify.pe"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="capacity">Capacidad</Label>
              <Input
                id="capacity"
                type="number"
                value={formData.capacity}
                onChange={(e) => setFormData({ ...formData, capacity: Number.parseInt(e.target.value) })}
                min="10"
                max="1000"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="status">Estado</Label>
              <Select
                value={formData.status}
                onValueChange={(value: "Activo" | "Inactivo") => setFormData({ ...formData, status: value })}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="Activo">Activo</SelectItem>
                  <SelectItem value="Inactivo">Inactivo</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>

          <div className="flex justify-end space-x-2 pt-4">
            <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
              Cancelar
            </Button>
            <Button onClick={handleSaveCenter}>{editingCenter ? "Actualizar" : "Crear"} Centro</Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  )
}
