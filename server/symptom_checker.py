import pandas as pd 
import numpy as np 
from sklearn import svm
from sklearn.svm import SVC

class SymptomChecker:

    def __init__(self):
        matrix_data = pd.read_csv('./data/sym_dis_matrix.csv', header=None)
        matrix_data.head()
        sparse_data = pd.read_csv('./data/diffsydiw.csv')
        #symptoms and diagnostics : 
        #Linked to matrix_data
        symptom_link_data = pd.read_csv('./data/sym_t.csv') #linked 
        diagnostic_link_data = pd.read_csv('./data/dia_t.csv') #linked 
        #For sparse_data
        symptom_sparse_data = pd.read_csv('./data/sym_3.csv') #sparse
        diagnostic_sparse_data = pd.read_csv('./data/dia_3.csv') #sparse
        #other : 
        symdesc_data = pd.read_csv('./data/symptoms2.csv') #desc
        diagnostic_title_data = pd.read_csv('./data/diagn_title.csv') #titles.
        self.matrix_data = matrix_data
        self.sparse_data = sparse_data
        self.symptom_sparse_data = symptom_sparse_data
        self.diagnostic_sparse_data = diagnostic_sparse_data
        self.diagnostics = diagnostic_title_data
        self.last_col = None
        self.clf = None
        
    
    def clean_data(self):
        #creating sparse matrix using svm : 
        self.matrix_data = np.array(self.matrix_data)
        self.matrix_data = np.delete(self.matrix_data, 0, 1)
        self.matrix_data = np.rot90(self.matrix_data, k=3)
        #print(self.matrix_data)
        
        #extract last col
        self.last_col = self.matrix_data[:,-1]
        #print(self.last_col)
        
        #delete last col : 
        self.matrix_data = np.delete(self.matrix_data, -1, 1)
        #print(self.matrix_data)

    def calc_weight(self):
        for x in self.sparse_data :
            compt = 0
            for j in self.last_col:
                if x[1] == j: 
                    self.matrix_data[compt][x[0]] *= x[2]
                compt+=1

    def train_model(self):  
        self.clean_data()
        self.calc_weight()
        self.clf = svm.SVC()
        self.last_col = self.last_col.astype('int')
        #print(self.last_col)
        #print(self.matrix_data)
        #print(len(self.matrix_data[0]))
        self.clf.fit(self.matrix_data, self.last_col)


    def ml_diagnostic(self, id_symptoms):
        temp_data = np.zeros(131)

        for id in id_symptoms:
            temp_data[id-1] = 1  
        return self.clf.predict([temp_data])

    def id_to_sym(self, id_symptom):
        return self.symptom_sparse_data.loc["id:":id_symptom]
    
    def id_to_diag(self, id_diag):
        row = self.diagnostics.loc[self.diagnostics["id"] == id_diag]
        response = row.iloc[0]["title"]
        return row.iloc[0]["title"].split('\x0b')[0]


