import requests
from bs4 import BeautifulSoup
import os
import re


# URL della pagina con la lista dei paper da scaricare (da modificare più volte il valore di filestart alla fine del link per consentire la visualizzazione di 300 articoli distribuiti su pagine consecutive diverse)
list_page_url = "https://arxiv.org/search/advanced?advanced=1&terms-0-operator=AND&terms-0-term=Pose+Estimation&terms-0-field=title&classification-computer_science=y&classification-physics_archives=all&classification-include_cross_list=include&date-filter_by=all_dates&date-year=&date-from_date=&date-to_date=&date-date_type=submitted_date&abstracts=show&size=200&order=-announced_date_first&start=1200"

# Crea una cartella per salvare gli articoli HTML
os.makedirs('homework1/sources', exist_ok=True)

# Funzione per verificare se un articolo HTML contiene tabelle
def contains_table(html_content):
    soup = BeautifulSoup(html_content, 'html.parser')
    return bool(soup.find('table'))  # Restituisce True se trova una tabella

# Funzione per trasformare l'arXiv in ar5iv
def sanitize_paper_link(url):
    url_ar5iv = url.replace("arxiv.org", "ar5iv.org")
    return url_ar5iv

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
    
    # Trova la lista dei blocchi contenenti i link ai paper
    paper_list = soup.find_all('p', class_= "list-title is-inline-block") 
    
    # Per ogni blocco nella lista dei blocchi
    for p_block in paper_list:
        a_block = p_block.find('a')
        paper_title = sanitize_filename(a_block.text)
        paper_url = sanitize_paper_link(a_block['href'])
        print(f"Processing: {paper_title} ({paper_url})")
    
        # Scarica la pagina HTML del paper
        paper_response = requests.get(paper_url)
        if paper_response.status_code == 200:
            html_content = paper_response.text
            if contains_table(html_content):
                html_path = f"homework1/sources/{paper_title}.html"
                readme_path = f"homework1/README.md"
                with open(html_path, 'w', encoding='utf-8') as f:
                    f.write(html_content)
                with open(readme_path,'a') as file:
                    file.write(f"- {paper_url}\n")
                print(f"Downloaded: {paper_title}\n")

else:
    print(f"Errore durante il caricamento della pagina: {response.status_code}")

print(f"\n\nDownload completato! Gli articoli con tabelle sono stati salvati nella cartella 'homework1/sources'.")
