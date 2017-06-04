const emailRegex = /[^\s@]+@[^\s@]+\.[^\s@]+/
class Validation {
  constructor() {
    this.errors = []
  }

  isValid = () => !this.errors.length

  validate = (cond, error) => {
    if (cond) return;
    this.errors.push(error)
  }

  nonEmpty = (name, value) => this.validate(!!value, `${name} should not be empty`)
  length = (name, value, length) => this.validate(value.length >= length, `${name} length should be at least ${length}`)
  email = (value) => this.validate(emailRegex.test(value), "Please provide a valid email")
  equalPasswords = (pass1, pass2) => 
    this.validate(pass1 === pass2, "Passwords don't match")
}

export default Validation;
