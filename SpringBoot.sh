curl -X POST http://localhost:8080/api/zip/springboot \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "javaVersion=17" \
  -d "nomProjet=HelloWorld" \
  --output springboot.zip