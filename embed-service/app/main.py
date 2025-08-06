from fastapi import FastAPI, Request
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer

app = FastAPI()
model = SentenceTransformer("all-MiniLM-L6-v2")

class EmbedRequest(BaseModel):
    text: str

@app.post("/embed")
async def embed(req: EmbedRequest):
    embedding = model.encode(req.text).tolist()
    return {"embedding": embedding}
