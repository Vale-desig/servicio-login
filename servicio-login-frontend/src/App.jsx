 import { useState } from "react";
 import './App.css'

 function App() {
  const [vista, setVista] = useState('inicio') 
  const [menuAbierto, setMenuAbierto] = useState(false)
  const [mostrarEliminar, setMostrarEliminar] = useState(false)
  const [confirmarEliminacion, setConfirmarEliminacion] = useState(false)
  const [contrasenaEliminar, setContrasenaEliminar] = useState('')
  const [correoUsuario, setCorreoUsuario] = useState('')
  const [correoRecuperacion, setCorreoRecuperacion] = useState('')
  const [codigoRecuperacion, setCodigoRecuperacion] = useState('')
  const [nuevaContrasena, setNuevaContrasena] = useState('')
  const [confirmarNuevaContrasena, setConfirmarNuevaContrasena] = useState('')
  const [enviandoRecuperacion, setEnviandoRecuperacion] = useState(false)

  const [formulario, setFormulario] = useState({
    nombre: '',
    apellido: '',
    correo: '',
    contrasena: '',
    confirmarContrasena: ''
  })

  const [login, setLogin] = useState({
    nombre: '',
    apellido: '',
    contrasena: ''
  })

  const [mensaje, setMensaje] = useState('')

  const manejarCambio = (e) => {
    const { name, value } = e.target

    setFormulario({
      ...formulario,
      [name]: value
    })

    setMensaje('')
  }

  const manejarCambioLogin = (e) => {
    const { name, value } = e.target

    setLogin({
      ...login,
      [name]: value
    })

    setMensaje('')
  }

  const manejarLogin = async (e) => {
    e.preventDefault()

    if (!login.nombre || !login.apellido || !login.contrasena) {
      setMensaje('Todos los campos son obligatorios.')
      return
    }

    console.log('Datos enviados al login:', login)

    try {
      const respuesta = await fetch(
        'http://localhost:8080/api/auth/login',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(login)
        }
      )

      const textoResultado = await respuesta.text()

      let resultado

      try {
        resultado = JSON.parse(textoResultado)
      } catch {
        resultado = textoResultado
      }

      if (respuesta.ok) {
        setCorreoUsuario(resultado.correo)
        setMensaje(resultado.mensaje)
        setVista('bienvenido')
      } else {
        setMensaje(resultado)
      }
    } catch {
      setMensaje(
        'No se pudo conectar con el servicio. Verifica que ell backend esté ejecutándose.'
      )
    }
  }

  const manejarRecuperacion = async (e) => {
    e.preventDefault()

    if (enviandoRecuperacion) {
      return
    }

    setEnviandoRecuperacion(true)

    if (!correoRecuperacion) {
      setMensaje('Debe ingresar su correo electrónico')
      return
    }

    try {
      const respuesta = await fetch(
        'http://localhost:8080/api/auth/recuperacion',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            correo: correoRecuperacion
          })
        }
      )

      const resultado = await respuesta.text()

      console.log('Respuesta de recuperación:', resultado)

      if (respuesta.ok) {
        setMensaje(resultado)
        setVista('restablecer')
      } else {
        setMensaje(resultado)
      }

    } catch {
      setMensaje(
        'No se pudo conectar con el servicio. Verifica que el backend esté ejecutándose.'
      )
    } finally {
      setEnviandoRecuperacion(false)
    }
  }

  const manejarRestablecer = async (e) => {
    e.preventDefault()

    if (
      !correoRecuperacion ||
      !codigoRecuperacion ||
      !nuevaContrasena ||
      !confirmarNuevaContrasena
    ) {
      setMensaje('Todos los campos son obligatorios.')
      return
    }

    try {
      const respuesta = await fetch(
        'http://localhost:8080/api/auth/restablecer',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            correo: correoRecuperacion,
            codigo: codigoRecuperacion,
            nuevaContrasena: nuevaContrasena,
            confirmarContrasena: confirmarNuevaContrasena
          })
        }
      )

      const resultado = await respuesta.text()

      console.log('Respuesta de restablecimiento:', resultado)

      setMensaje(resultado)

      if (respuesta.ok) {
        setVista('login')
        setCorreoRecuperacion('')
        setCodigoRecuperacion('')
        setNuevaContrasena('')
        setConfirmarNuevaContrasena('')
      }

    } catch {
      setMensaje(
        'No se pudo conectar con el servicio. Verifica que el backend esté ejecutándose.'
      )
    }
  }

  const manejarRegistro = async (e) => {
    e.preventDefault()

    if (
      !formulario.nombre ||
      !formulario.apellido ||
      !formulario.correo ||
      !formulario.contrasena ||
      !formulario.confirmarContrasena
    ) {
      setMensaje('Todos los campos son obligatorios.')
      return
    }

    if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñÜü ]+$/.test(formulario.nombre)) {
      setMensaje('El nombre solo puede contener letras y espacios.')
      return
    }

    if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñÜü ]+$/.test(formulario.apellido)) {
      setMensaje('El apellido solo puede contener letras y espacios.')
      return
    }

    if (formulario.contrasena.length < 8) {
      setMensaje('La contraseña debe tener mínimo 8 caracteres.')
      return
    }

    if (
      !/[A-Z]/.test(formulario.contrasena) ||
      !/[a-z]/.test(formulario.contrasena) ||
      !/[0-9]/.test(formulario.contrasena) ||
      !/[^A-Za-z0-9\s]/.test(formulario.contrasena)
    ) {
      setMensaje(
        'La contraseña debe tener mayúscula, minúscula, número y carácter especial.'
      )
      return
    }

    if (formulario.contrasena !== formulario.confirmarContrasena) {
      setMensaje('Las contraseñas no coinciden.')
      return
    }

    try {
      const respuesta = await fetch(
        'http://localhost:8080/api/auth/registro',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(formulario)
        }
      )

      const resultado = await respuesta.text()

      if (respuesta.ok) {
        setMensaje('Registro realizado correctamente.')
        setFormulario({
          nombre: '',
          apellido: '',
          correo: '',
          contrasena: '',
          confirmarContrasena: ''
        })
      } else {
        setMensaje(resultado)
      }
    } catch {
      setMensaje(
        'No se pudo conectar con el servicio. Verifica que el backend esté ejecutándose.'
      )
    }
  }

  const manejarEliminarCuenta = async () => {

    console.log('Se hizo clic en Eliminar cuenta')

    if (!contrasenaEliminar) {
      setMensaje('Debe ingresar su contraseña.')
      return
    }

    try {
      
      console.log('Correo enviado para eliminar:', correoUsuario)

      const respuesta = await fetch(
        'http://localhost:8080/api/auth/eliminar-cuenta',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            correo: correoUsuario,
            contrasena: contrasenaEliminar
          })
        }
      )

      const resultado = await respuesta.text()

      console.log('Respuesta de eliminar cuenta.', resultado)

      if (respuesta.ok) {
        setMensaje('Cuenta eliminada correctamente.')

        setConfirmarEliminacion(false)
        setContrasenaEliminar('')
        setCorreoUsuario('')
        setLogin({
          nombre: '',
          apellido: '',
          contrasena: ''
        })
        setVista('inicio')
      } else {
        setMensaje(resultado)
      }

    } catch {
      setMensaje(
        'No se pudo conectar con el servicio. Verifica que el backend esté ejecutándose.'
      )
    }
  }

  if (vista === 'registro') {
    return (
      <main className="pagina-formulario">
        <div className="tarjeta-formulario">
          <h1>Crear cuenta</h1>

          <p>Completa los siguientes datos para registrarte</p>

          <form onSubmit={manejarRegistro}>
            <label htmlFor="nombre">Nombre</label>
            <input 
            id="nombre"
            name="nombre"
            type="text"
            placeholder="Ingrese su nombre"
            value={formulario.nombre}
            onChange={manejarCambio}
          />

          <label htmlFor="apellido">Apellido</label>
          <input 
          id="apellido"
          name="apellido"
          type="text"
          placeholder="Ingrese su apellido"
          value={formulario.apellido}
          onChange={manejarCambio}
          />

          <label htmlFor="correo">Correo electrónico</label>
          <input 
          id="correo"
          name="correo"
          type="email"
          placeholder="Ingrese su correo electrónico"
          value={formulario.correo}
          onChange={manejarCambio}
          />

          <label htmlFor="contrasena">Contraseña</label>
          
          <input 
          id="contrasena"
          name="contrasena"
          type="password"
          placeholder="Ingrese su contraseña"
          value={formulario.contrasena}
          onChange={manejarCambio}
          />

          {formulario.contrasena &&
            !(
              formulario.contrasena.length >= 8 &&
              /[A-Z]/.test(formulario.contrasena) &&
              /[a-z]/.test(formulario.contrasena) &&
              /[0-9]/.test(formulario.contrasena) &&
              /[^A-Za-z0-9\s]/.test(formulario.contrasena)
            ) && (
              <ul className="reglas-contrasena">
                <li className={formulario.contrasena.length >= 8 ? 'cumplida' : ''}>
                  Mínimo 8 caracteres
                </li>

                <li className={/[A-Z]/.test(formulario.contrasena) ? 'cumplida' : ''}>
                  Una letra mayúscula
                </li>

                <li className={/[a-z]/.test(formulario.contrasena) ? 'cumplida' : ''}>
                  Una letra minúscula
                </li>

                <li className={/[0-9]/.test(formulario.contrasena) ? 'cumplida' : ''}>
                  Un número
                </li>

                <li className={/[^A-Za-z0-9\s]/.test(formulario.contrasena) ? 'cumplida' : ''}>
                  Un carácter especial
                </li>
              </ul>
            )}

          <label htmlFor="confirmarContrasena">
            Confirmar contraseña
          </label>

          <input 
          id="confirmarContrasena"
          name="confirmarContrasena"
          type="password"
          placeholder="Confirme su contraseña"
          value={formulario.confirmarContrasena}
          onChange={manejarCambio}
          />

          {mensaje && (
            <p className="mensaje-formulario">
              {mensaje}
            </p>
          )}

          <button type="submit">
            Registrarse
          </button>
          </form>

          <button
          type="button"
          className="boton-secundario"
          onClick={() => {
            setVista('inicio')
            setMensaje('')
          }}
          >
            Volver
          </button>
        </div>
      </main>
    )
  }

  if (vista === 'login') {
    return (
      <main className="pagina-formulario">
        <div className="tarjeta-formulario">
          <h1>Iniciar sesión</h1>

          <p>Ingresa tus datos para acceder.</p>

          <form onSubmit={manejarLogin}>
            <label htmlFor="loginNombre">Nombre</label>

            <input 
            id="loginNombre"
            name="nombre"
            type="text"
            placeholder="Ingrese su nombre"
            value={login.nombre}
            onChange={manejarCambioLogin}
            />

            <label htmlFor="loginApellido">Apellido</label>

            <input 
            id="loginApellido"
            name="apellido"
            type="text"
            placeholder="Ingrese su apellido"
            value={login.apellido}
            onChange={manejarCambioLogin}
            />

            <label htmlFor="loginContrasena">Contraseña</label>

            <input 
            id="loginContrasena"
            name="contrasena"
            type="password"
            placeholder="Ingrese su contraseña"
            value={login.contrasena}
            onChange={manejarCambioLogin}
            />
            
            <a
            type="#"
            className="enlace-recuperacion"
            onClick={(e) => {
              e.preventDefault()
              setVista('recuperacion')
              setMensaje('')
            }}
            >
              ¿Olvidaste tu contraseña?
            </a>

            {mensaje && (
              <p className="mensaje-formulario">
                {mensaje}
              </p>
            )}

            <button type="submit">
              Iniciar sesión
            </button>
            </form>

          <button
          type="button"
          className="boton-secundario"
          onClick={() => {
            setVista('inicio')
            setMensaje('')
          }}
          >
            Volver
          </button>
        </div>
      </main>
    )
  }

  if (vista === 'recuperacion') {
    return (
      <main className="pagina-formulario">
        <div className="tarjeta-formulario">

          <h1>Recuperar contraseña</h1>

          <p>
            Ingresa tu correo electrónico para recibir un código de recuperación.
          </p>

          <form onSubmit={manejarRecuperacion}>

            <label htmlFor="correoRecuperacion">
              Correo electrónico
            </label>

            <input 
            id="correoRecuperacion"
            type="email"
            placeholder="Ingrese su correo"
            value={correoRecuperacion}
            onChange={(e) => setCorreoRecuperacion(e.target.value)}
            />

            <button 
            type="submit"
            disabled={enviandoRecuperacion}
            >
              {enviandoRecuperacion ? 'Enviando código...' : 'Enviar código'}
            </button>
          </form>

          {mensaje && (
            <p className="mensaje-formulario">
              {mensaje}
            </p>
          )}

          <button
          type="button"
          className="boton-secundario"
          onClick={() => {
            setVista('login')
            setMensaje('')
          }}
          >
            Cancelar
          </button>
        </div>
      </main>
    )
  }

  if (vista === 'restablecer') {
    return (
      <main className="pagina-formulario">
        <div className="tarjeta-formulario">

          <h1>Restablecer contraseña</h1>

          <p>
            Ingrese el código recibido en tu correo y crea una nueva contraseña.
          </p>

          <form onSubmit={manejarRestablecer}>

            <label htmlFor="codigoRecuperacion">
              Código de recuperación
            </label>

            <input 
            id="codigoRecuperacion"
            type="text"
            placeholder="Ingrese el código"
            value={codigoRecuperacion}
            onChange={(e) => setCodigoRecuperacion(e.target.value)}
            />

            <label htmlFor="nuevaContrasena">
              Nueva contraseña
            </label>

            <input id="nuevaContrasena"
            type="password"
            placeholder="Ingrese su nueva contraseña"
            value={nuevaContrasena}
            onChange={(e) => setNuevaContrasena(e.target.value)}
            />

            <label htmlFor="confirmarNuevaContrasena">
              Confirmar nueva contraseña
            </label>

            <input 
            id="confirmarNuevaContrasena"
            type="password"
            placeholder="Confirme su nueva contraseña"
            value={confirmarNuevaContrasena}
            onChange={(e) => setConfirmarNuevaContrasena(e.target.value)}
            />

            {mensaje && (
              <p className="mensaje-formulario">
                {mensaje}
              </p>
            )}

            <button type="submit">
              Restablecer contraseña
            </button>
          </form>

          <button
          type="button"
          className="boton-secundario"
          onClick={() => {
            setVista('login')
            setMensaje('')
          }}
          >
            Cancelar
          </button>
        </div>
      </main>
    )
  }

  if (vista === 'bienvenido') {
    return (
      <main className="pagina-formulario">

        <button
        type="button"
        className="boton-menu"
        onClick={() => setMenuAbierto(true)}
        >
          ☰
        </button>

        {menuAbierto && (
          <aside className="menu-lateral">

            <button
            type="button"
            className="boton-cerrar-menu"
            onClick={() => setMenuAbierto(false)}
            >
              ✕ 
            </button>

            <h2>Menú</h2>

            <button 
            type="button"
            onClick={() => {
              setVista('inicio')
              setMenuAbierto(false)
              setMensaje('')
              setLogin({
                nombre: '',
                apellido: '',
                contrasena: ''
              })
            }}
            >
              Cerrar sesión
            </button>

            <button 
            type="button"
            onClick={() => {
              setMostrarEliminar(true)
              setMenuAbierto(false)
            }}
            >
              Eliminar cuenta
            </button>

          </aside>
        )}

        <div className="tarjeta-formulario">
          <h1>¡Bienvenido!</h1>
          <p>Has iniciado sesión correctamente.</p>

          {mensaje && (
            <p className="mensaje-formulario">
              {mensaje}
            </p>
          )}
        </div>

        {mostrarEliminar && (
          <div className="ventana-confirmacion">
            <div className="tarjeta-confirmacion">

              <h2>Eliminar cuenta</h2>

              <p>
                ¿Estás segura de que deseas eliminar tu cuenta?
              </p>

              <p>
                Esta acción no se puede deshacer.
              </p>

              <button
              type="button"
              onClick={() => setMostrarEliminar(false)}
              >
                Cancelar
              </button>

              <button
              type="button"
              onClick={() => {
                setMostrarEliminar(false)
                setConfirmarEliminacion(true)
              }}
              >
                Continuar
              </button>

            </div>
          </div>
        )}

        {confirmarEliminacion && (
          <div className="ventana-confirmacion">
            <div className="tarjeta-confirmacion">

              <h2>Confirmar eliminación</h2>

              <p>
                Para eliminar tu cuenta, ingresa tu contraseña.
              </p>

              <input 
              type="password" 
              placeholder="Ingrese su contraseña"
              value={contrasenaEliminar}
              onChange={(e) => setContrasenaEliminar(e.target.value)}
              />

              <button
              type="button"
              onClick={() => {
                setConfirmarEliminacion(false)
              }}
              >
                Cancelar
              </button>

              <button
              type="button"
              onClick={manejarEliminarCuenta}
              >
                Eliminar cuenta
              </button>

            </div>
          </div>
        )}

      </main>
    )
  }

  return (
    <main className="inicio">
      <div className="tarjeta-inicio">
        <h1>Servicio de Inicio de Sesión</h1>

        <p>
          Regístrate o inicia sesión para continuar.
        </p>

        <div className="botones-inicio">
          <button
          type="button"
          onClick={() => setVista('registro')}
          >
            Registrarse
          </button>

          <button 
          type="button"
          onClick={() => {
            setVista('login')
            setMensaje('')
          }}
          >
            Iniciar sesión
          </button>
        </div>
      </div>
    </main>
  )
}

export default App