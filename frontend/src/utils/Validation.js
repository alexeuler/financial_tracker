import { indexOf } from 'ramda';

const emailRegex = /[^\s@]+@[^\s@]+\.[^\s@]+/
class Validation {
  constructor(value) {
    this.value = value;
    this.errors = []
  }

  isValid = () => !this.errors.length

  validate = (cond, error) => {
    if (cond) return;
    this.errors.push(error)
  }

  nonEmpty = () => this.validate(!!this.value, `Field should not be empty`)
  length = (length) => this.validate(this.value.length >= length, `Length should be at least ${length}`)
  lengthOrEmpty = (length) => this.validate(!this.value || this.value.length >= length, `Length should be at least ${length} or field should be omitted by using zero length`)
  email = () => this.validate(emailRegex.test(this.value), "Please provide a valid email")
  number = () => this.validate(!isNaN(parseInt(this.value, 0)), "Please provide a numeric value")
  oneOf = values => this.validate(indexOf(this.value, values) >= 0, `Please specify one of the following values: ${values}`)
  equalPasswords = (pass1, pass2) => 
    this.validate(pass1 === pass2, "Passwords don't match")
}

export default Validation;
