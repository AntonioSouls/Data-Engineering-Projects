import requests
from bs4 import BeautifulSoup
import os
import re


# URL della pagina con la lista dei paper da scaricare
list_page_url = "https://arxiv.org/list/cs.LG/recent?skip=0&show=400"

# Crea una cartella per salvare gli articoli HTML
os.makedirs('homework1/sources', exist_ok=True)

# Funzione per verificare se un articolo HTML contiene tabelle
def contains_table(html_content):
    soup = BeautifulSoup(html_content, 'html.parser')
    return bool(soup.find('table'))  # Restituisce True se trova una tabella

# Funzione per sanificare il nome del file
def sanitize_filename(filename):
    # Rimuovi i caratteri non validi per i nomi di file, come \n, \t, : etc.
    filename = re.sub(r'[\\/*?:"<>|]', "_", filename)
    filename = re.sub(r'\s+', '_', filename)  # Sostituisce spazi multipli o newline con underscore
    return filename


#####################################################################################################################################

# Scarica la pagina contenente la lista dei paper
response = requests.get(list_page_url)
if response.status_code == 200:
    soup = BeautifulSoup(response.text, 'html.parser')
    
    # Trova tutti i blocchi della pagina che contengono tutti i possibili formati di un paper
    paper_blocks = soup.find_all('dt')  # Cerca i link che puntano ai singoli paper
    
    # Per ogni blocco nella lista dei blocchi
    for block in paper_blocks:
        link_preview = block.find('a', title="Abstract")  # Seleziono il link della preview
        link_HTML = block.find('a', title="View HTML")    # Seleziono il link dell'HTML da scaricare
        if link_HTML is not None:
            paper_url = link_HTML['href']  # Costruisci il link completo
            paper_title = sanitize_filename(link_preview.text)  # Usa la funzione per sanificare il nome file
            print(f"Processing: {paper_title} ({paper_url})")
    
            # Scarica la pagina HTML del paper
            paper_response = requests.get(paper_url)
            if paper_response.status_code == 200:
                html_content = paper_response.text
                if contains_table(html_content):
                    html_path = f"homework1/sources/{paper_title}.html"
                    with open(html_path, 'w', encoding='utf-8') as f:
                        f.write(html_content)
                    print(f"Downloaded: {paper_title}\n")

else:
    print(f"Errore durante il caricamento della pagina: {response.status_code}")

print(f"\n\nDownload completato! Gli articoli con tabelle sono stati salvati nella cartella 'homework1/sources'.")
