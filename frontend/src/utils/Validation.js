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

  nonEmpty = (value) => this.validate(!!value, `Field should not be empty`)
  length = (value, length) => this.validate(value.length >= length, `Length should be at least ${length}`)
  email = (value) => this.validate(emailRegex.test(value), "Please provide a valid email")
  equalPasswords = (pass1, pass2) => 
    this.validate(pass1 === pass2, "Passwords don't match")
}

export default Validation;
