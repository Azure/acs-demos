import unittest
import sys

from summaryTable import SummaryTable

class TestSummaryTable(unittest.TestCase):

  @classmethod
  def setUpClass(self):
    self.event_type = "test"
    self.table = SummaryTable()

  def test_create(self):
    raised = False
    try: 
      self.table.getCount(self.event_type)
    except Exception, err:
      sys.stderr.write('ERROR: %sn' % str(err))
      raised = True
    self.assertFalse(raised, 'Exception raised')
      
  def test_count(self):
    orig_count = self.table.getCount(self.event_type)
    count = orig_count + 1
    self.table.updateCount(self.event_type, count)
    new_count = self.table.getCount(self.event_type)
    self.assertEqual(new_count, orig_count + 1)

if __name__ == '__main__':
    unittest.main()
