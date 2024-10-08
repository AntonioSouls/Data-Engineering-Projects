import os
import json
from lxml import etree
from email import message_from_file, message_from_bytes
import re
from base64 import b64decode
from bs4 import BeautifulSoup

def extract_html_from_mhtml(file_path):
    try:
        with open(file_path, 'rb') as file:  # Apri il file in modalita'  binaria
            msg = message_from_bytes(file.read())  # Carica il messaggio MHTML
            
            # Trova la parte con contenuto HTML
            for part in msg.walk():
                if part.get_content_type() == "text/html":
                    # Decodifica correttamente il payload
                    return part.get_payload(decode=True).decode('utf-8')
    
    except Exception as e:
        print(f"Errore durante l'estrazione di {file_path}: {e}")
    
    return None  # Se non trova la parte HTML, restituisce None

# Funzione principale per convertire tutti i file MHTML in HTML
def convert_mhtml_to_html(papers_folder, output_folder):
    # Crea la cartella di output se non esiste
    os.makedirs(output_folder, exist_ok=True)
    
    # Scorri tutti i file nella cartella papers
    for filename in os.listdir(papers_folder):
        if filename.endswith(".mhtml"):
            file_path = os.path.join(papers_folder, filename)
            print(f"Processing: {file_path}")  # Stampa il nome del file che stai processando
            
            # Estrai il contenuto HTML dal file MHTML
            html_content = extract_html_from_mhtml(file_path)
            
            if html_content:
                # Salva il contenuto HTML in un nuovo file nella cartella di output
                html_filename = f"{os.path.splitext(filename)[0]}.html"  # Rimuovi l'estensione .mhtml
                html_file_path = os.path.join(output_folder, html_filename)
                
                with open(html_file_path, 'w', encoding='utf-8') as html_file:
                    html_file.write(html_content)  # Scrivi il contenuto HTML
                print(f"Saved HTML to: {html_file_path}")
            else:
                print(f"Errore: impossibile estrarre il contenuto HTML da {filename}")

# Definisci le cartelle
papers_folder = 'sources_mhtml'  # Cartella dei file MHTML
output_folder = 'sources'           # Cartella per i file HTML

# Esegui la conversione
convert_mhtml_to_html(papers_folder, output_folder)