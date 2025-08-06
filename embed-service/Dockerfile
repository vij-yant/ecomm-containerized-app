# Use a lightweight Python image
FROM python:3.11-slim

# Set working directory inside container
WORKDIR /app

# Copy requirements first for caching
COPY app/requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application
COPY app/ .

# Expose FastAPI port (adjust if different)
EXPOSE 8000

# Run the FastAPI app
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
