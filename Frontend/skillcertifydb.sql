-- Base de datos para SkillCertify
-- Sistema de certificación profesional con gestión de prerrequisitos

-- Tabla de usuarios
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    company VARCHAR(255),
    role ENUM('ADMIN', 'NORMAL', 'INSTRUCTOR') DEFAULT 'NORMAL',
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de centros de capacitación
CREATE TABLE centers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    address VARCHAR(500),
    city VARCHAR(100),
    region VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(255),
    director VARCHAR(255),
    capacity INT DEFAULT 100,
    students_count INT DEFAULT 0,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de categorías de evaluación
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    order_index INT NOT NULL,
    color VARCHAR(50),
    icon VARCHAR(10),
    prerequisite_category_id BIGINT,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (prerequisite_category_id) REFERENCES categories(id)
);

-- Tabla de evaluaciones
CREATE TABLE evaluations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category_id BIGINT NOT NULL,
    duration_minutes INT NOT NULL,
    difficulty ENUM('BASICO', 'INTERMEDIO', 'AVANZADO') NOT NULL,
    passing_score INT DEFAULT 70,
    status ENUM('ACTIVE', 'INACTIVE', 'DRAFT') DEFAULT 'DRAFT',
    prerequisite_evaluation_id BIGINT,
    max_attempts INT DEFAULT 3,
    time_limit_enabled BOOLEAN DEFAULT TRUE,
    shuffle_questions BOOLEAN DEFAULT TRUE,
    show_results_immediately BOOLEAN DEFAULT TRUE,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (prerequisite_evaluation_id) REFERENCES evaluations(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Tabla de preguntas
CREATE TABLE questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    evaluation_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    question_type ENUM('MULTIPLE_CHOICE', 'TRUE_FALSE', 'OPEN_TEXT') NOT NULL,
    points INT DEFAULT 1,
    order_index INT,
    explanation TEXT,
    difficulty ENUM('EASY', 'MEDIUM', 'HARD') DEFAULT 'MEDIUM',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (evaluation_id) REFERENCES evaluations(id) ON DELETE CASCADE
);

-- Tabla de opciones de respuesta
CREATE TABLE question_options (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN DEFAULT FALSE,
    order_index INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

-- Tabla de intentos de evaluación
CREATE TABLE evaluation_attempts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    evaluation_id BIGINT NOT NULL,
    attempt_number INT NOT NULL,
    status ENUM('IN_PROGRESS', 'COMPLETED', 'ABANDONED', 'EXPIRED') DEFAULT 'IN_PROGRESS',
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    time_spent_minutes INT,
    score INT DEFAULT 0,
    max_score INT DEFAULT 0,
    percentage DECIMAL(5,2) DEFAULT 0.00,
    passed BOOLEAN DEFAULT FALSE,
    certified BOOLEAN DEFAULT FALSE,
    ip_address VARCHAR(45),
    user_agent TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (evaluation_id) REFERENCES evaluations(id),
    UNIQUE KEY unique_user_evaluation_attempt (user_id, evaluation_id, attempt_number)
);

-- Tabla de respuestas del usuario
CREATE TABLE user_answers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    attempt_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option_id BIGINT,
    text_answer TEXT,
    is_correct BOOLEAN DEFAULT FALSE,
    points_earned INT DEFAULT 0,
    time_spent_seconds INT,
    flagged BOOLEAN DEFAULT FALSE,
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (attempt_id) REFERENCES evaluation_attempts(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id),
    FOREIGN KEY (selected_option_id) REFERENCES question_options(id)
);

-- Tabla de certificados
CREATE TABLE certificates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    evaluation_id BIGINT NOT NULL,
    attempt_id BIGINT NOT NULL,
    certificate_code VARCHAR(50) UNIQUE NOT NULL,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    status ENUM('ACTIVE', 'REVOKED', 'EXPIRED') DEFAULT 'ACTIVE',
    verification_url VARCHAR(500),
    pdf_path VARCHAR(500),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (evaluation_id) REFERENCES evaluations(id),
    FOREIGN KEY (attempt_id) REFERENCES evaluation_attempts(id)
);

-- Tabla de asignación de usuarios a centros
CREATE TABLE user_centers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    center_id BIGINT NOT NULL,
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ACTIVE', 'INACTIVE', 'GRADUATED') DEFAULT 'ACTIVE',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (center_id) REFERENCES centers(id),
    UNIQUE KEY unique_user_center (user_id, center_id)
);

-- Tabla de asignación de evaluaciones a centros
CREATE TABLE center_evaluations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    center_id BIGINT NOT NULL,
    evaluation_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    FOREIGN KEY (center_id) REFERENCES centers(id),
    FOREIGN KEY (evaluation_id) REFERENCES evaluations(id),
    UNIQUE KEY unique_center_evaluation (center_id, evaluation_id)
);

-- Tabla de conversaciones del chatbot
CREATE TABLE chatbot_conversations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    session_id VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    sender ENUM('USER', 'BOT') NOT NULL,
    intent VARCHAR(100),
    confidence DECIMAL(3,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabla de configuración del sistema
CREATE TABLE system_settings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    description TEXT,
    updated_by BIGINT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (updated_by) REFERENCES users(id)
);

-- Tabla de logs de auditoría
CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    table_name VARCHAR(50),
    record_id BIGINT,
    old_values JSON,
    new_values JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Índices para optimización
CREATE INDEX idx_evaluations_category ON evaluations(category_id);
CREATE INDEX idx_evaluations_status ON evaluations(status);
CREATE INDEX idx_questions_evaluation ON questions(evaluation_id);
CREATE INDEX idx_attempts_user_evaluation ON evaluation_attempts(user_id, evaluation_id);
CREATE INDEX idx_attempts_status ON evaluation_attempts(status);
CREATE INDEX idx_certificates_user ON certificates(user_id);
CREATE INDEX idx_certificates_code ON certificates(certificate_code);
CREATE INDEX idx_user_answers_attempt ON user_answers(attempt_id);
CREATE INDEX idx_chatbot_user_session ON chatbot_conversations(user_id, session_id);
CREATE INDEX idx_audit_logs_user_action ON audit_logs(user_id, action);

-- Datos iniciales
INSERT INTO categories (name, description, order_index, color, icon) VALUES
('Fundamentos de Programación', 'Conceptos básicos de programación y lógica', 1, 'bg-blue-100 text-blue-800 border-blue-200', '💻'),
('Desarrollo Web Frontend', 'HTML, CSS, JavaScript y frameworks modernos', 2, 'bg-green-100 text-green-800 border-green-200', '🌐'),
('Desarrollo Web Backend', 'Servidores, APIs y bases de datos', 3, 'bg-purple-100 text-purple-800 border-purple-200', '⚙️'),
('Desarrollo Full Stack', 'Integración completa de tecnologías web', 4, 'bg-orange-100 text-orange-800 border-orange-200', '🚀'),
('Diseño UX/UI', 'Experiencia de usuario y diseño de interfaces', 5, 'bg-pink-100 text-pink-800 border-pink-200', '🎨');

UPDATE categories SET prerequisite_category_id = 1 WHERE id = 2;
UPDATE categories SET prerequisite_category_id = 2 WHERE id = 3;
UPDATE categories SET prerequisite_category_id = 3 WHERE id = 4;

INSERT INTO system_settings (setting_key, setting_value, description) VALUES
('max_attempts_per_evaluation', '3', 'Número máximo de intentos por evaluación'),
('certificate_expiry_months', '24', 'Meses de validez de los certificados'),
('min_passing_score', '70', 'Puntuación mínima para aprobar'),
('chatbot_enabled', 'true', 'Habilitar chatbot en la plataforma'),
('email_notifications', 'true', 'Enviar notificaciones por email');